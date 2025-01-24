(ns polytask.database.interface
  (:require [polytask.database.in-memory-database.core :as core]))

(defn insert!
  "Inserts an entity into the database"
  [db table entity]
  (core/insert! db table entity))

(defn find-unique
  "Returns a unique entity in the database by id"
  [db table id]
  (core/find-unique db table id))

(defn find-many
  "Returns all entities in the database.
  If a criteria is provided, returns entities that match the criteria."
  ([db table] (core/find-many db table))
  ([db table criteria] (core/find-many db table criteria)))

(defn update!
  "Updates an entity in the database.
  If the entity does not exist, returns nil"
  [db table id entity]
  (core/update! db table id entity))

(defn delete!
  "Deletes an entity from the database"
  [db table id]
  (core/delete! db table id))
