(ns polytask.http-client.core
  (:require
   [courier.http :as http]
   [jsonista.core :as json]))

(defn parse-response
  "Parses JSON response body into Clojure data."
  [response]
  (try
    (-> response
        :body
        (json/read-value (json/object-mapper {:decode-key-fn true})))
    (catch #?(:clj Exception
              :cljs js/Error) e
      {:error "Invlid JSON response"
       :exception (ex-message e)})))

(defn handle-error
  "Handles HTTP errors and returns structured error response."
  [response]
  {:status (:status response)
   :error  (parse-response response)})

(defn request
  "Make an HTTP request."
  [{:keys [method url headers body params]}]
  (let [request {:req (merge {:method method
                              :url url
                              :headers (merge {"Content-Type" "application/json"} headers)}
                             (when body {:body (json/write-value-as-string body)})
                             (when params {:params params}))}
        response (http/request request)]
    (if (:success? response)
      (parse-response response)
      (handle-error response))))
