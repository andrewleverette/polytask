(ns development.core
  (:require
   [integrant.core :as ig]
   [polytask.config.interface :as c]
   [polytask.http-client.interface :as http-client]))

(comment
  (def config (c/get-config :polytask.api/system))

  (def system (ig/init config))
  system
  (ig/halt! system))

(http-client/request
 {:method :get
  :url "http://localhost:8000/tasks"})

(http-client/request
 {:method :get
  :url "http://localhost:8000/tasks/5f811e2e-05cc-4cf6-bd55-adf51e5090a3"})

(http-client/request
 {:method :put
  :url "http://localhost:8000/tasks/5f811e2e-05cc-4cf6-bd55-adf51e5090a3"
  :body {:status "in-progress"}})

(http-client/request
 {:method :post
  :url "http://localhost:8000/tasks"
  :body {:title "Study Clojure for 1 hour"
         :description "Using the curring current learn clojure goals and milestones, study Clojure for 1 hour."
         :status "not-started"}})
