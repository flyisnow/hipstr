(ns hipstr.models.posts
  (:refer-clojure :exclude [get])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(def mysql-db {:subprotocol "mysql"
               :subname "//localhost:3306/raycenter"
               :user "root"
               :password "123456"
               :zeroDateTimeBehaviour "convertToNull"})

(def now 
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

(defn all []
  (j/query mysql-db
           (s/select * :posts)))

(defn get [id]
  (j/query mysql-db
           (s/select * :posts
                     (s/where {:id id}))))

(defn create [params]
  (j/insert! mysql-db :posts (merge params {:created_at now :updated_at now})))

(defn save [id params]
  (j/update! mysql-db :posts params (s/where {:id id})))

(defn delete [id]
  (j/delete! mysql-db :posts (s/where {:id id})))

(all)

(get 2)
