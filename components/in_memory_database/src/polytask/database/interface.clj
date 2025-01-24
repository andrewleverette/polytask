(ns polytask.database.interface
  (:import java.util.UUID))

(defn insert!
  "Inserts an entity into the database"
  [db table entity]
  (let [id (str (UUID/randomUUID))
        new-entity (assoc entity :id id)]
    (swap! (:state db) assoc-in [table id] new-entity)
    new-entity))

(defn find-unique
  "Returns a unique entity in the database by id"
  [db table id]
  (get-in (:state db) [table id]))

(defn find-many
  "Returns all entities in the database.
  If a criteria is provided, returns entities that match the criteria."
  ([db table] (-> @(:state db) (get table {}) vals))
  ([db table criteria]
   (let [entities (-> @(:state db) (get table {}) vals)]
     (filter criteria entities))))

(defn update!
  "Updates an entity in the database"
  [db table id entity]
  (let [existing-entity (find-unique db table id)
        updated-entity (merge existing-entity entity)]
    (swap! (:state db) assoc-in [table id] updated-entity)
    updated-entity))

(defn delete!
  "Deletes an entity from the database"
  [db table id]
  (swap! (:state db) update table dissoc id))
