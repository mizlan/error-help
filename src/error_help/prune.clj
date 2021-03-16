(ns error-help.prune
  (:require [clojure.data.json :as json]
            [error-help.collect :as collect]
            [clojure.pprint :as pp]))

(defn prune-errors
  "Takes a JSON list and prunes out the
  irrelevant errors/messages. Returns a
  non-nested list"
  [xs])
(defn process-output
  "Processes the output of the compiling
  shell command (stderr). Assume the stderr is valid JSON."
  [output]
  (->> output
       :err
       json/read-json))
