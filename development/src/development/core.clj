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
 {:req {:method :get
        :url "http://localhost:8000/tasks"}})

(http-client/request
 {:req {:method :post
        :url "http://localhost:8000/tasks"
        :body "{\"title\": \"Work on PolyTask\"
                 \"description\" \"Currently need to build an API Client that supports CLJ and CLJS\"
                 \"status\" \"not started\"}"}})
