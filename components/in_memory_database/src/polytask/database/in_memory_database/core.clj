(ns polytask.database.in-memory-database.core
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :polytask.db/in-memory [_ _]
  (println "Starting in-memory database")
  {:state (atom {})})

(defmethod ig/halt-key! :polytask.db/in-memory [_ db]
  (reset! (:state db) nil))

(defn- timestamp
  "Returns the current timestamp"
  []
  (java.time.Instant/now))

(defn- generate-UUID
  "Generates a UUID as a string"
  []
  (str (java.util.UUID/randomUUID)))

(defn insert!
  "Inserts an entity into the database"
  [db table entity]
  (let [id (generate-UUID)
        new-entity (assoc entity
                          :id id
                          :created_at (timestamp))]
    (swap! (:state db) assoc-in [table id] new-entity)
    new-entity))

(defn find-unique
  "Returns a unique entity in the database by id"
  [db table id]
  (get-in @(:state db) [table id]))

(defn find-many
  "Returns all entities in the database.
  If a criteria is provided, returns entities that match the criteria."
  ([db table] (or (-> @(:state db) (get table {}) vals vec) []))
  ([db table criteria]
   (let [entities (or (-> @(:state db) (get table {}) vals vec) [])]
     (filter criteria entities))))

(defn update!
  "Updates an entity in the database
  If the entity does not exist, returns nil"
  [db table id entity]
  (when-let [existing-entity (find-unique db table id)]
    (let [updated-entity (assoc (merge existing-entity entity)
                                :updated_at (timestamp))]
      (swap! (:state db) assoc-in [table id] updated-entity)
      updated-entity)))

(defn delete!
  "Deletes an entity from the database
  If the entity does not exist, returns false"
  [db table id]
  (if (find-unique db table id)
    (do
      (swap! (:state db) update table dissoc id)
      true)
    false))
