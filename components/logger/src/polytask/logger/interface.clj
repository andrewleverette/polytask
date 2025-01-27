(ns polytask.logger.interface
  (:require [polytask.logger.core :as l]))

(def info
  "Log informational messages"
  (partial l/log :info))

(def debug
  "Log debug messages"
  (partial l/log :debug))

(def warn
  "Log warning messages"
  (partial l/log :warn))

(def error
  "Log error messages"
  (partial l/log :error))
