(defproject clojure-rest-example "0.1.0-SNAPSHOT"

  :description "Simple Clojure REST service to translate numbers into words."
  
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :repl-options {:port 4555}

  :plugins [[lein-ring "0.8.11"] ]

  :ring {:handler clojure-rest-example.rest/app
         :nrepl   {:start? true
                   :port   4555}}
  
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; Tools
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.clojure/tools.trace "0.7.9"]                 
                 [com.taoensso/timbre "4.3.1"]
                 [slingshot "0.12.2"]
                 
                 ;; REST
                 [cheshire "5.5.0"]
                 [compojure "1.5.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 
                 ;; Testing
                 [ring/ring-mock "0.3.0"]]
  
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]
                   :source-paths ["dev"]}})
