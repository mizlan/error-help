(ns error-help.translate
  (:require [error-help.translate-util :as util]
            [error-help.translations :as tra]))

(defn translate-message
  ([msg] (translate-message msg tra/translators))
  ([msg translation-dict]
   (some #(let [pat (:pattern %)
                sub-func (:replacement %)]
            (some->> msg
                     (re-find pat)
                     sub-func))
         translation-dict)))
