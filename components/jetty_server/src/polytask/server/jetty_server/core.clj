(ns polytask.server.jetty-server.core
  (:require
   [ring.adapter.jetty :as jetty]
   [polytask.logger.interface :as l]))

(defn start
  "Starts a Jetty server instance with the 
  given handler and configuration."
  [handler config]
  (let [host (or (:host config) "localhost")
        port (or (:port config) 8000)]
    (l/info (str "Starting server on " host ":" port))
    (jetty/run-jetty handler config)))

(defn stop
  "Stops a Jetty server instance."
  [server]
  (when server
    (l/info "Stopping server"
            (.stop server))))
