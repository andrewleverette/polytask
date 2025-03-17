(ns polytask.logger.interface
  (:require [polytask.logger.core :as core]))

(defn debug
  [logger event]
  (core/log logger (assoc event :level :debug)))

(defn info
  [logger event]
  (core/log logger (assoc event :level :info)))

(defn warn
  [logger event]
  (core/log logger (assoc event :level :warn)))

(defn error
  [logger event]
  (core/log logger (assoc event :level :error)))
