(ns error-help.core
  (:gen-class)
  (:require [error-help.translate-util :refer [translate]]
            [error-help.prune :as prune]))

(defn -main
  [& args]
  (let [filepath (first args)]
    (println "input file: " filepath)
    (let [errors (prune/get-errors filepath)]
      (map #(translate (:message %)) (errors)))))
