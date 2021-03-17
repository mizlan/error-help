(ns error-help.translate)

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

(def example-msg "'Int' was not declared in this scope; did you mean 'int'?")

(println (translate example-msg))
