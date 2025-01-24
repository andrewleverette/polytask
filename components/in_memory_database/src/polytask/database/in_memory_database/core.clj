(ns polytask.database.in-memory-database.core
  (:require [integrant.core :as ig])
  (:import java.util.UUID))

(defmethod ig/init-key :polytask.db/in-memory [_ _]
  {:state (atom {})})

(defmethod ig/halt-key! :polytask.db/in-memory [_ db]
  (reset! (:state db) nil))

(defn generate-UUID
  "Generates a UUID as a string"
  []
  (str (UUID/randomUUID)))
