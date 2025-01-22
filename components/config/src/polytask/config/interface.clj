(ns polytask.config.interface
  (:require [polytask.config.core :as core]))

(defn get-config
  "Returns the value of a configuration key for the current environment config."
  ([ks] (core/get-config core/config ks))
  ([ks default] (core/get-config core/config ks default)))
