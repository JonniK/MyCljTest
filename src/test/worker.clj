(ns test.worker
  (:require [zeromq.zmq :as zmq])
  (:use flatland.protobuf.core)
  )

(defn -main []
  (println "Starting worker...")
  (import protobuf.test.Test$Person)
  (def Person (protodef protobuf.test.Test$Person))
  (let [context (zmq/zcontext)]
    (with-open [responder (doto (zmq/socket context :rep)
                            (zmq/connect "tcp://127.0.0.1:5560"))]
      (while (not (.. Thread currentThread isInterrupted))
        (let [p (protobuf-load Person (zmq/receive responder))
              nP (assoc p :likes ["climbing" "running" "jumping"])
              nB (protobuf-dump nP)]
          (Thread/sleep 10)
          (zmq/send responder nB))))))