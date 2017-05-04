(defproject hipstr "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://defn.win"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.8.7"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-ttl-session "0.1.1"]
                 [ring "1.4.0"
                  :exclusions [ring/ring-jetty-adapter]]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [org.webjars/bootstrap "3.3.5"]
                 [org.webjars/jquery "2.1.4"]
                 [org.immutant/web "2.0.2"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [ring-basic-authentication "1.0.2"]]

  :min-lein-version "2.0.0"
  :uberjar-name "hipstr.jar"
  :jvm-opts ["-server"]

  :main hipstr.core

  :plugins [[lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [cider/cider-nrepl "0.8.1"]
           ]
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[ring/ring-mock "0.2.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.7.0"]]
                  
                  
                  :repl-options {:init-ns hipstr.core}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  ;;when :nrepl-port is set the application starts the nREPL server on load
                  :env {:dev        true
                        :port       3000
                        :nrepl-port 7000}}
   :project/test {:env {:test       true
                        :port       3001
                        :nrepl-port 7001}}
   :profiles/dev {}
   :profiles/test {}})
