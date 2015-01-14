(ns test.worker
  (:require [zeromq.zmq :as zmq]
            [taoensso.carmine :as car :refer (wcar)])
  (:use flatland.protobuf.core))

(def server1-conn {:pool {} :spec {}}) ; See `wcar` docstring for opts
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(import pbuf.test.Test$Person)
(import pbuf.test.Test$Request)
(import pbuf.test.Test$Response)
(def Person (protodef pbuf.test.Test$Person))
(def Request (protodef pbuf.test.Test$Request))
(def Response (protodef pbuf.test.Test$Response))


(defn -main []
  (println "Starting worker...")
  (let [context (zmq/zcontext)]
    (with-open [responder (doto
                            (zmq/socket context :rep)
                            (zmq/connect "tcp://127.0.0.1:5560"))]
      (while (not (.. Thread currentThread isInterrupted))
        (try
          (let [request (protobuf-load Request (zmq/receive responder))
                _ (println request)
                response (protobuf Response :action (:action request)
                                   :result (case (:action request)
                                             "get" "OK"
                                             "set" (wcar* (car/hmset* "person" (:person request)))
                                             "Wrong action")
                                   :person (case (:action request)
                                             "get" (wcar* (car/hgetall "person"))
                                             nil))]
            (println response)
            (Thread/sleep 10)
            (zmq/send responder (protobuf-dump response)))
          (catch Exception e
            (zmq/send responder (protobuf-dump (protobuf Response
                                                         :action "Error"
                                                         :result (str "caught exception: " (.getMessage e)))))))))))