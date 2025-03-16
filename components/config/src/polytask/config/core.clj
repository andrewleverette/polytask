;; Copyright (C) 2025 Andrew Leverette. All rights reserved.

;; This software is governed by the Eclipse Public License v2.0
;; (https://www.eclipse.org/legal/epl-2.0/), which can be found in the
;; LICENSE file at the root of this distribution. By using this software
;; in any fashion, you are agreeing to be bound by the terms of this license.

;; You must not remove this notice, or any other, from this software.

(ns polytask.config.core
  "Component for managing application configuration.

  This component is responsible for loading and providing access to
  the application's configuration settings. It loads configuration from
  environment-specific EDN files located in the `env/` directory.

  The configuration is loaded once when the component is initialized and
  can be refreshed at runtime if needed.  It provides functions to retrieve
  configuration values by keys or key paths.

  Configuration files are expected to be in EDN format and named according
  to the environment (e.g., `env/local.env.edn`, `env/dev.env.edn`, `env/prod.env.edn`).
  The environment is determined by the `POLYTASK_ENV` environment variable,
  defaulting to 'local' if the variable is not set."
  (:require
   [clojure.io :as io]
   [integrant.core :as ig]))

(defn- get-env
  []
  (or (System/getenv "POLYTASK_ENV") "local"))

(defn- parse-config-str
  [str]
  (let [content (ig/read-string str)]
    (if (map? content)
      content
      (throw (ex-info "Invalid config file. Expected a map" {:content content})))))

(defn- read-file-content
  [filepath]
  (let [f (io/file filepath)]
    (if (.exists f)
      (slurp f)
      (throw (ex-info "Config file not found" {:filepath filepath})))))

(defn- read-env-file
  [env]
  (let [filepath (str "env/" env ".env.edn")]
    (->> filepath
         read-file-content
         parse-config-str)))

(defn- load-config []
  (let [env (get-env)]
    (try
      (read-env-file env)
      (catch Exception e
        (throw (ex-info "Error loading config" {:env env} e))))))

(def ^:private config
  "PolyDoc configuration map"
  (load-config))

(defn get-config
  ([ks] (get-config ks nil))
  ([ks default]
   (let [s (if (sequential? ks) ks [ks])]
     (get-in config s default))))

(defn refresh
  []
  (alter-var-root #'config (constantly (load-config))))
