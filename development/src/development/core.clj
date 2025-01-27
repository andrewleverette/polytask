(ns development.core
  (:require
   [integrant.core :as ig]))

(comment
  (def config
    {:polytask.db/in-memory nil
     :task-api/server {:db (ig/ref :polytask.db/in-memory)}})

  (def system (ig/init config))
  system
  (ig/halt! system))
