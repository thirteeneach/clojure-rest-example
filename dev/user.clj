(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require [clojure
             [pprint :refer [pprint print-table]]
             [reflect :refer [reflect]]
             [repl :refer [apropos dir doc find-doc pst source]]
             [stacktrace :refer [print-stack-trace]]
             [test :as test]]
            [clojure-rest-example
             [rest :as rest]
             [system :as system]]
            [clojure.java.javadoc :refer [javadoc]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [clojure.tools.trace :refer [trace-ns untrace-ns]]
            [ring.adapter.jetty :as jetty]))

(defn start-server []
  (def server (jetty/run-jetty rest/app {:port 3000 :join? false})))

(defn print-methods [x]
  (->> x
       reflect
       :members 
       (filter #(contains? (:flags %) :public))
       (sort-by :name)
       print-table))

(def system
  "A Var containing an object representing the application under development."
  nil)

(defn init
  "Creates and initializes the system under development in the Var #'system."
  []
  (alter-var-root #'system
                  (constantly (system/system))))

(defn start
  "Starts the system running, updates the Var #'system."
  []
  (alter-var-root #'system system/start))

(defn stop
  "Stops the system if it is currently running, updates the Var #'system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (system/stop s)))))

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))
