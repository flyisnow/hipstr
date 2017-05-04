(ns hipstr.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes GET POST]]
            [hipstr.routes.home :refer [home-routes]]
            [hipstr.middleware :as middleware]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [ring.middleware.basic-authentication :refer :all]
            [ring.util.response :as resp]
            [hipstr.models.posts :as posts-model]
            [hipstr.controllers.posts :as posts-controller]
            [hipstr.controllers.admin.posts :as admin-posts-controller]))



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
  (GET "/" [] "Hello My new blog")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app-base
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    #'base-routes))

(def app (middleware/wrap-base #'app-base))

;;add
(defn authenticated? [name pass]
  (and (= name "user")
       (= "pass" pass)))

(defroutes public-routes
  (GET "/" [] (posts-controller/index))
  (route/resources "/"))

(defroutes protected-routes
  (GET "/admin" [] (admin-posts-controller/index))
  (GET "/admin/:id" [id] (admin-posts-controller/show id))
  (GET "/admin/posts/new" [] (admin-posts-controller/new))
  (POST "/admin/posts/create" [& params]
        (do (posts-model/create params)
            (resp/redirect "/admin")))
  (GET "/admin/posts/:id/edit" [id] (admin-posts-controller/edit id))
  (POST "/admin/posts/:id/save" [& params]
        (do (posts-model/save (:id params) params)
            (resp/redirect "/admin")))
  (GET "/admin/posts/:id/delete" [id]
       (do (posts-model/delete id)
           (resp/redirect "/admin")))
)


(defroutes app-routes
  public-routes
  (wrap-basic-authentication protected-routes authenticated?)
  ;; (GET "/" [] (posts-controller/index))
  ;; (route/resources "/")
  (route/not-found "Not Found"))

(def app 
  (handler/site app-routes))
