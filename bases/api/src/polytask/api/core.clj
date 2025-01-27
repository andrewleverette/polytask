(ns polytask.api.core
  (:require [integrant.core :as ig]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.adapter.jetty :as jetty]
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
              (println "Handling GET /tasks")
              (println "DB State: " db)
              (let [tasks (task-repo/get-all-tasks db)]
                (println "Tasks: " tasks)
                {:status 200 :body {:tasks tasks}}))
       :post (fn [{:keys [body-params]}]
               (let [task (task-repo/create-task! db body-params)]
                 {:status 201
                  :body task}))}]
     ["/tasks/:id"
      {:get (fn [{{:keys [id]} :path-params}]
              (println "Handling GET /tasks/:id")
              (println "DB State: " db)
              (if-let [task (task-repo/get-task db id)]
                {:status 200 :body task}
                {:status 404 :body {:error "Task not found"}}))
       :put (fn [{:keys [path-params body-params]}]
              (println "Handling PUT /tasks/:id")
              (println "DB State: " db)
              (if-let [task (task-repo/update-task! db (:id path-params) body-params)]
                {:status 200 :body task}
                {:status 404 :body {:error "Task not found"}}))
       :delete (fn [{{:keys [id]} :path-params}]
                 (println "Handling DELETE /tasks/:id")
                 (println "DB State: " db)
                 (if (task-repo/delete-task! db id)
                   {:status 204}
                   {:status 404 :body {:error "Task not found"}}))}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn- start-server [context]
  (jetty/run-jetty (routes context) {:port 3000 :join? false}))

(defn- stop-server [server]
  (.stop server))

(defmethod ig/init-key :task-api/server [_ {:keys [db]}]
  (println "Starting server on port 3000")
  (let [server (start-server {:db db})]
    {:server server}))

(defmethod ig/halt-key! :task-api/server [_ {:keys [server]}]
  (when server
    (println "Stopping server")
    (stop-server server)))
