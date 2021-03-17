(ns error-help.core
  (:gen-class)
  (:require [error-help.translate :refer [translate-message]]
            [error-help.prune :as prune]))

(defn -main
  [& args]
  (let [filepath (first args)]
    (println "input file: " filepath)
    (let [errors (prune/get-errors filepath)]
      (println "done getting" (count errors) "errors")
      (run! println (map #(translate-message (:message %)) errors))
      (shutdown-agents))))
