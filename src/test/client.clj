(ns test.client
  (:require [zeromq.zmq :as zmq])
  (:use flatland.protobuf.core))

(import pbuf.test.Test$Person)
(import pbuf.test.Test$Request)
(import pbuf.test.Test$Response)
(def Person (protodef pbuf.test.Test$Person))
(def Request (protodef pbuf.test.Test$Request))
(def Response (protodef pbuf.test.Test$Response))


(defn -main [act & args]
  (println "Starting client...")
  (try (let [context (zmq/zcontext)
             data (first args)
             _ (println data)
             p (protobuf Request
                         :action act
                         :person (cond
                                   (= act "set") (protobuf Person :id (:id data) :name (:name data))
                                   :else nil))
             d (protobuf-dump p)]
         (with-open [requester (doto
                                 (zmq/socket context :req)
                                 (zmq/connect "tcp://127.0.0.1:5559"))]
           (println "Req" p)
           (zmq/send requester d)
           (let [r (zmq/receive requester)]
             (println "Resp" (protobuf-load Response r))
             )))
       (catch Exception e (str "caught exception: " (.getMessage e))))
  )
