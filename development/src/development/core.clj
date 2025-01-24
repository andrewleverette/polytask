(ns development.core
  (:require
   [integrant.core :as ig]
   [polytask.task-repository.interface :as task-repo]))

(def config
  {:polytask.db/in-memory {}})

(def system (ig/init config))

(def db-instance (:polytask.db/in-memory system))

db-instance

(task-repo/get-all-tasks db-instance)

(task-repo/create-task! db-instance {:name "Test task" :description "This is a test task"})
(def task (first (task-repo/get-all-tasks db-instance)))
task

(task-repo/get-task db-instance (:id task))
(def updated-task (task-repo/update-task! db-instance (:id task) {:name "Updated task" :status "todo"}))

(task-repo/get-all-tasks db-instance)

(ig/halt! system)
