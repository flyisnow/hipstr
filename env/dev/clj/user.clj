(ns user
  (:require [mount.core :as mount]
            hipstr.core))

(defn start []
  (mount/start-without #'hipstr.core/repl-server))

(defn stop []
  (mount/stop-except #'hipstr.core/repl-server))

(defn restart []
  (stop)
  (start))


