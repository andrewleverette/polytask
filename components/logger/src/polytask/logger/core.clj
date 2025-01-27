(ns polytask.logger.core
  (:require [taoensso.telemere :as t]
            [polytask.config.interface :as c]))

(t/set-min-level! :log (c/get-config :logger/min-level :info))

(defn log
  "Wrapper for Telemere's log!"
  ([level msg] (t/log! level msg))
  ([level msg data] (t/log! {:level level :data data} msg)))
