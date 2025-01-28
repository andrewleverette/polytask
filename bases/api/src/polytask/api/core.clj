(ns polytask.api.core
  (:gen-class)
  (:require [integrant.core :as ig]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.adapter.jetty :as jetty]
            [polytask.config.interface :as c]
            [polytask.logger.interface :as l]
            [polytask.task-repository.interface :as task-repo]))

(defn routes [{:keys [db]}]
  (ring/ring-handler
   (ring/router
    [["/ping"
      {:get (fn [_]
              {:status 200
               :body {:message "OK"}})}]
     ["/tasks"
      {:get (fn [_]
              (l/info "Handling GET /tasks")
              (let [tasks (task-repo/get-all-tasks db)]
                (l/debug (str "Found: " (count tasks)) {:tasks tasks})
                {:status 200 :body {:tasks tasks}}))
       :post (fn [{:keys [body-params]}]
               (l/info "Handling POST /tasks")
               (let [task (task-repo/create-task! db body-params)]
                 (l/debug "Created task" {:task task})
                 {:status 201
                  :body task}))}]
     ["/tasks/:id"
      {:get (fn [{{:keys [id]} :path-params}]
              (l/info "Handling GET /tasks/:id")
              (if-let [task (task-repo/get-task db id)]
                (do
                  (l/debug "Found task" {:task task})
                  {:status 200 :body task})
                (do
                  (l/warn "Task not found" {:id id})
                  {:status 404 :body {:error "Task not found"}})))
       :put (fn [{:keys [path-params body-params]}]
              (l/info "Handling PUT /tasks/:id")
              (if-let [task (task-repo/update-task! db (:id path-params) body-params)]
                (do
                  (l/debug "Updated task" {:task task})
                  {:status 200 :body task})
                (do
                  (l/warn "Task not found" {:id (:id path-params)})
                  {:status 404 :body {:error "Task not found"}})))
       :delete (fn [{{:keys [id]} :path-params}]
                 (l/info "Handling DELETE /tasks/:id")
                 (if (task-repo/delete-task! db id)
                   (do
                     (l/debug "Deleted task" {:id id})
                     {:status 204})
                   (do
                     (l/warn "Task not found" {:id id})
                     {:status 404 :body {:error "Task not found"}})))}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn- start-server [deps]
  (let [config (c/get-config :server)]
    (l/info (str "Starting server on " (or (:host config) "localhost") ":" (:port config)))
    (jetty/run-jetty (routes deps) config)))

(defn- stop-server [server]
  (l/info "Stopping server")
  (.stop server))

(defmethod ig/init-key :polytask.api/ring [_ context]
  (let [server (start-server context)]
    {:server server}))

(defmethod ig/halt-key! :polytask.api/ring [_ {:keys [server]}]
  (when server
    (stop-server server)))

(defn -main
  []
  (let [config (c/get-config :polytask.api/system)]
    (if (map? config)
      (ig/init config)
      (do
        (l/error "Failed to load config" {:config config})
        (System/exit 1)))))
