(ns error-help.translate
  (:import
   [java.util.regex Pattern])
  (:require [clansi :refer [style]]
            [jansi-clj.core :refer :all]
            [clojure.string :as str]))

;; Thanks to https://github.com/AvisoNovate/pretty/blob/master/src/io/aviso/ansi.clj
(def ^:const ^:private ansi-pattern (Pattern/compile "\\e\\[.*?m"))

(defn ^String strip-ansi
  "Removes ANSI codes from a string, returning just the raw text."
  [string]
  (str/replace string ansi-pattern ""))

(defn space-separate
  [& strs]
  (str/join " " strs))

(defn prune-cpp-namespaces
  "Deletes leading namespace(s) as common in C++"
  [token]
  (str/replace token #".+::" ""))

(defn code-style
  [s]
  (style s :blue :bold))

(defn a-an
  "Conditionally use `a` or `an`. The English language sucks."
  [s]
  (if (re-find #"^\s*[aeiou]" (strip-ansi s))
    (str "an " s)
    (str "a " s)))

(defn translate
  ([msg] (translate msg translators))
  ([msg translation-dict]
   (some #(let [pat (:pattern %)
                sub-func (:replacement %)]
            (some->> msg
                     (re-find pat)
                     sub-func))
         translation-dict)))
