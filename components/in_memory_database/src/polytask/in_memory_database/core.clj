(ns polytask.in-memory-database.core
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :polytask.db/in-memory [_ _]
  {:state (atom {})})

(defmethod ig/halt-key! :polytask.db/in-memory [_ db]
  (reset! (:state db) nil))
