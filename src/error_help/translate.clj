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

(def translators
  [{:label "Easier types"
    :pattern #"^no match for 'operator(.+?)' \(operand types are '(.+?)' and '(.+?)'\)$"
    :replacement
    (fn [matches]
      (let [groups (rest matches)
            operator (first groups)
            atype (prune-cpp-namespaces (second groups))
            btype (prune-cpp-namespaces (last groups))]
        (cond
          (= operator "=")
          (space-separate
           "You tried to assign" (a-an (code-style btype))
           "to" (a-an (code-style atype)))
          :else
          (space-separate
           "You tried to use" (code-style operator)
           "on" (a-an (code-style atype)) "and"
           (a-an (code-style btype)) "which is illegal"))))}

   {:label "Undeclared variable with hint"
    :pattern #"^'(\w+)' was not declared in this scope; did you mean '(\w+)'\?$"
    :replacement
    (fn [matches]
      (let [var (second matches)
            alternative (last matches)]
        (space-separate
         "I could not find where you defined" (code-style var)
         "but I think you meant" (code-style alternative))))}

   {:label "Undeclared variable"
    :pattern #"^'(\w+)' was not declared in this scope$"
    :replacement
    (fn [matches]
      (let [var (second matches)]
        "I could not find where you defined" (code-style var)))}])

(defn translate
  ([msg] (translate msg translators))
  ([msg translation-dict]
   (some #(let [pat (:pattern %)
                sub-func (:replacement %)]
            (some->> msg
                     (re-find pat)
                     sub-func))
         translation-dict)))

(def example-msg "'Int' was not declared in this scope; did you mean 'int'?")

(println (translate example-msg))
