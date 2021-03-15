(ns error-help.collect
  (:require [clojure.java.shell :as shell]))

(def default-compiler-executable "g++-10")

(defn compilation-output
  "Get the compilation output using a compiler executable (must be in $PATH, has a definable default value) and a filepath"
  ([filepath] (compilation-output default-compiler-executable filepath))
  ([executable filepath] (shell/sh
                          executable
                          "-Wall"
                          "-fdiagnostics-format=json"
                          filepath)))
