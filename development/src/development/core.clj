(ns development.core
  (:require
   [integrant.core :as ig]
   [polytask.config.interface :as c]))

(comment
  (def config (c/get-config :polytask.api/system))

  (def system (ig/init config))
  system
  (ig/halt! system))
