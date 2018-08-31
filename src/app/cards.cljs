(ns app.cards
  (:require
    [devcards.core]
    ; Load namespaces with `defcard` or `deftest` definitions
    [app.core]))

(devcards.core/start-devcard-ui!)
