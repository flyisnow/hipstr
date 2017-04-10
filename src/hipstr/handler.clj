(ns hipstr.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [hipstr.routes.home :refer [home-routes]]
            [hipstr.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     (if (env :dev) :trace :info)
     :appenders {:rotor (rotor/rotor-appender
                          {:path "hipstr.log"
                           :max-size (* 512 1024)
                           :backlog 10})}})

  (if (env :dev) (parser/cache-off!))
  (timbre/info (str
                 "\n-=[hipstr started successfully"
                 (when (env :dev) " using the development profile")
                 "]=-")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "hipstr is shutting down...")
  (timbre/info "shutdown complete!"))

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app-base
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    #'base-routes))

(def app (middleware/wrap-base #'app-base))
