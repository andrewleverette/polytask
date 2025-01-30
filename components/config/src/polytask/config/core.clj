(ns polytask.config.core
  (:require
   [clojure.java.io :as io]
   [integrant.core :as ig]))

(def default-config
  "{:environment :local
     :server {:port 3000 :join? false}
     :polytask.api/system {:polytask.db/in-memory {}
                           :polytask.api/ring {:db #ig/ref :polytask.db/in-memory}}}")

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
  (let [content (->> file slurp ig/read-string)]
    (if (map? content)
      content
      {})))

(defn- read-env-file
  "Given an environment keyword, return a map of the environment file contents
  stored in an edn file."
  [env]
  (let [config-file (str "config/" (name env) ".env.edn")
        file (io/resource config-file)]
    (if file
      (parse-env-file file)
      (throw (Exception. (str "Could not find config file: " config-file))))))

(defn- fetch-from-secrets-manager
  "Fetches secrets from AWS Secrets Manager or HashiCorp Vault.
  This is just a placeholder function"
  []
  ;; Simulate fetching secrets
  {})

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
   (let [s (if (sequential? ks) ks [ks])]
     (get-in config s default))))

(def config
  "The polytask configuration map."
  (load-config))
