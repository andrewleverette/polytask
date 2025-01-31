(ns polytask.server.interface
  (:require
   [polytask.server.jetty-server.core :as jetty-server]))

(defn start
  [handler config]
  (jetty-server/start handler config))

(defn stop
  [server]
  (jetty-server/stop server))
