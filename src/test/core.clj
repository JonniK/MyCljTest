(ns test.core
  (:gen-class)
  (:require [test
             [broker :as b]
             [worker :as w]
             [client :as c]])
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")

  ;(b/-main)
  ;(future (w/-main))
  ;(c/-main)
  )

