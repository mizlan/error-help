(ns error-help.prune
  (:require [clojure.data.json :as json]
            [error-help.collect :as collect]
            [clojure.pprint :as pp]))

(defn matches-location
  "Check if all files in a location map correspond to
  `filepath`"
  [filepath locmap]
  (let [locs (vals locmap)
        results (map (comp (partial = filepath) :file) locs)]
    (println results)
    (and results)))
(defn is-relevant
  "Determine if an error|message|note object is relevant,
  by seeing if their locations are the actual source file"
  [filepath obj]
  (let [locs (:locations obj)]
    (println "these are the locs" locs)
    ;; using map here, but often just 1 location
    (let [res (and (map (partial matches-location filepath) locs))]
      (println "this is the res" res)
      res)))

(defn prune-errors
  "Takes a JSON list and prunes out the
  irrelevant errors/messages. needs filepath to help determine
  if error messages are relevant. Returns a
  non-nested list where each element is shape
  {:kind <string> :message <string>}"
  [filepath xs]
  (if (empty? xs) []
      (let [child-errors (prune-errors filepath (mapcat :children xs))]
        (concat child-errors (filter (partial is-relevant filepath) xs)))))

(defn output-to-map
  "Processes the output of the compiling
  shell command (stderr). Assume the stderr is valid JSON."
  [output]
  (-> output
       :err
       (json/read-str :key-fn keyword)))
