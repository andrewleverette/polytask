;; Copyright (C) 2025 Andrew Leverette. All rights reserved.

;; This software is governed by the Eclipse Public License v2.0
;; (https://www.eclipse.org/legal/epl-2.0/), which can be found in the
;; LICENSE file at the root of this distribution. By using this software
;; in any fashion, you are agreeing to be bound by the terms of this license.

;; You must not remove this notice, or any other, from this software.

(ns polytask.logger.core
  "Component for managing application logging.

  This component provides functions for logging messages at different
  levels of severity, such as info, debug, warn, and error."
  (:require
   [taoensso.telemere :as t]
   [polytask.config.interface :as c]))

(defprotocol Logger
  (log [this message]))

(defrecord ConsoleLogger [config]
  Logger
  (log [this event]
    (let [{:keys [message level]} event]
      (t/log! {:level level
               :data (dissoc event :message :level)}
              message))))

(defn get-logger-config [] (c/get-config :polytask/logger))

(defmulti create-logger
  (fn [tag] tag))

(defmethod create-logger :console
  []
  (let [config (get-logger-config)]
    (t/set-min-level! (get config :min-level :debug))
    (->ConsoleLogger config)))

