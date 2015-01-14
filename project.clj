(defproject test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.flatland/protobuf "0.8.1"]
                 [org.zeromq/cljzmq "0.1.4"]
                 [com.taoensso/carmine "2.9.0"]
                 ]
  :plugins [[lein-protobuf "0.4.1"]]
  :main ^:skip-aot test.core
  :target-path  "target"
  :resource-path "resources"
  :proto-path "resources/proto"
  ;:profiles {:uberjar {:aot :all}}
  :jvm-opts ["-Djava.library.path=/usr/lib:/usr/local/lib"]
  )
