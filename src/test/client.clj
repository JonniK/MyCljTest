(ns test.client
  (:require [zeromq.zmq :as zmq])
  (:use flatland.protobuf.core)
  )

(defn -main []
  (println "Starting client...")
  (import protobuf.test.Test$Person)
  (def Person (protodef protobuf.test.Test$Person))
  (let [context (zmq/zcontext)]
    (with-open [requester (doto (zmq/socket context :req)
                            (zmq/connect "tcp://127.0.0.1:5559"))]
        (def p (protobuf Person :id 4 :name "Bob" :email "bob@example.com"))
        (println "Req" p)
        (def d (protobuf-dump p))
        (zmq/send requester d)
        (let [r (zmq/receive requester)]
          (println "Resp" (protobuf-load Person r))
          )
        )
    )
  )
