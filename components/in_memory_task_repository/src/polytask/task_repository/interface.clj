(ns polytask.task-repository.interface
  (:require [polytask.task-repository.in-memory-task-repository.core :as core]))

(defn create-task!
  "Creates a task in the database"
  [db task]
  (core/create-task! db task))

(defn get-task
  "Gets a task from the database by id"
  [db id]
  (core/get-task db id))

(defn get-all-tasks
  "Gets all tasks from the database"
  [db]
  (core/get-all-tasks db))

(defn update-task!
  "Updates a task in the database"
  [db id task]
  (core/update-task! db id task))

(defn delete-task!
  "Deletes a task from the database"
  [db id]
  (core/delete-task! db id))
