(ns error-help.translate
  (:import
   [java.util.regex Pattern])
  (:require [clansi :refer [style]]
            [jansi-clj.core :refer :all]
            [clojure.string :as str]))
