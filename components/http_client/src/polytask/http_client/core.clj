(ns polytask.http-client.core
  (:require [courier.http :as http]))

(defn request
  "Make an HTTP request."
  [req]
  (http/request req))
