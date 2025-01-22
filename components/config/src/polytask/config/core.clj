(ns polytask.config.core
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(defn- get-env
  "Returns the current environment with a default of :local
  if the POLYTASK_ENV environment variable is not set."
  []
  (keyword (or (System/getenv "POLYTASK_ENV") "local")))

(defn- get-config-source
  "Returns the configuration source. Defaults to :edn if POLYTASK_CONFIG_SOURCE is not set."
  []
  (keyword (System/getenv "POLYTASK_CONFIG_SOURCE") "edn"))

(defn- parse-env-file
  "Given a file, return the parse contents of the config file."
  [file]
  (let [content (->> file slurp edn/read-string)]
    (if (map? content)
      content
      {})))

(defn- read-env-file
  "Given an environment keyword, return a map of the environment file contents
  stored in an edn file."
  [env]
  (let [config-file (str "./config/" (name env) ".env.edn")]
    (when-let [file (io/resource config-file)]
      (try
        (parse-env-file file)
        (catch Exception _
          {})))))

(defn- fetch-from-secrets-manager
  "Fetches secrets from AWS Secrets Manager or HashiCorp Vault.
  This is just a placeholder function"
  []
  ;; Simulate fetching secrets
  {"DATABASE_URL" "postgres://user:password@secrets-db:5432/polytask"
   "API_KEY" "super-secret-key"
   "SECRET_FEATURE_FLAG" "true"})

(defn load-config
  "Load the configuration for the current environment based on the config source."
  []
  (case (get-config-source)
    :edn (read-env-file (get-env))
    :secrets (fetch-from-secrets-manager)
    {}))

(defn get-config
  "Given a configuration map and a sequence of one or more keys, 
  return the value at the key path"
  ([config ks] (get-config config ks nil))
  ([config ks default]
   (let [keys (if (sequential? ks) ks [ks])]
     (get-in config keys default))))

(def config
  "The polytask configuration map."
  (load-config))

