(ns clojure-rest-example.rest
  (:require [clojure-rest-example
             [numbers :as numbers]
             [utils :refer [def-]]]
            [compojure
             [core :refer [context defroutes GET POST]]
             [route :as route]]
            [ring.middleware
             [content-type :refer [wrap-content-type]]
             [defaults :refer [wrap-defaults]]
             [json :refer [wrap-json-body wrap-json-params wrap-json-response]]]
            [ring.util.response :refer [content-type not-found redirect response]]
            [taoensso.timbre :as timbre]
            [slingshot.slingshot :refer [throw+ try+]]
            [ring.util.http-response :refer [bad-request]]))

(timbre/refer-timbre)

(defn- with-response-defaults [response]
  (-> response
      (content-type "text/plain")))

(defn- parse-number [s]
  (try+
   (Long/valueOf s)
   (catch java.lang.NumberFormatException ex
     (throw+ {:type :bad-request}))))

(defn- translate-number [params]
  (with-response-defaults
    (let [num (parse-number (params :number))]
      (if (<= 0 num 1000)
        (response (numbers/num-to-words num))
        (bad-request)))))

(defroutes app-routes
  (context "/translate" []
    (POST "/" {params :params} (translate-number params)))
  
  (route/resources "/")
  (route/not-found (not-found "Not found")))

(def- default-config
  {:params {:urlencoded true
            :keywordize true
            :nested     true
            :multipart  true}

   :responses {:not-modified-responses true
               :absolute-redirects     true
               :content-types          true}})

(defn- wrap-log-request [handler]
  (fn [req]
    (debug "Handling request" req)
    (handler req)))

(defn- wrap-transform-exceptions [handler]
  (fn [req]
    (try+
     (handler req)
     (catch [:type :bad-request] ex
       (bad-request)))))

(def app
  (-> app-routes
      (wrap-log-request)
      (wrap-defaults default-config)
      (wrap-json-body)
      (wrap-json-params)
      (wrap-json-response)
      (wrap-content-type)
      (wrap-transform-exceptions)))
