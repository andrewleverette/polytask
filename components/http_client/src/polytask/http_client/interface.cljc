(ns polytask.http-client.interface
  (:require [polytask.http-client.core :as core]))

(defn request
  "Make an HTTP request."
  [req]
  (core/request req))
