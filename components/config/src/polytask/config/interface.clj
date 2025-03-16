;; Copyright (C) 2025 Andrew Leverette. All rights reserved.

;; This software is governed by the Eclipse Public License v2.0
;; (https://www.eclipse.org/legal/epl-2.0/), which can be found in the
;; LICENSE file at the root of this distribution. By using this software
;; in any fashion, you are agreeing to be bound by the terms of this license.

;; You must not remove this notice, or any other, from this software.

(ns polytask.config.interface
  (:require [polytask.config.core :as core]))

(defn get-config
  "Given a sequence of keys, return the value of the config at those keys.
  Returns nil or default if not found."
  ([ks] (core/get-config ks))
  ([ks default] (core/get-config ks default)))

(defn refresh
  "Refresh the config"
  []
  (core/refresh))
