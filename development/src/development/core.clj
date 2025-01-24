(ns development.core
  (:require
   [integrant.core :as ig]
   [polytask.database.interface :as db]))

(def config
  {:polytask.db/in-memory {}})

(def system (ig/init config))

(def db-instance (:polytask.db/in-memory system))

(def task (db/insert! db-instance :db/tasks {:name "Task 1" :description "Task 1 Description"}))
task
(db/find-many db-instance :db/tasks)
(db/find-unique db-instance :db/tasks (:id task))

db-instance

(ig/halt! system)
