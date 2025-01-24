(ns polytask.task-repository.in-memory-task-repository.core
  (:require [polytask.database.interface :as db]))

(defn create-task!
  "Creates a task in the database"
  [db task]
  (db/insert! db :db/tasks task))

(defn get-task
  "Gets a task from the database by id"
  [db id]
  (db/find-unique db :db/tasks id))

(defn get-all-tasks
  "Gets all tasks from the database"
  [db]
  (db/find-many db :db/tasks))

(defn update-task!
  "Updates a task in the database"
  [db id task]
  (db/update! db :db/tasks id task))

(defn delete-task!
  "Deletes a task from the database"
  [db id]
  (db/delete! db :db/tasks id))
