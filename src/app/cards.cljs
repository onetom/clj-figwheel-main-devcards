(ns app.cards
  (:require
    [devcards.core]
    ; Load namespaces with cards
    [app.core]))

(devcards.core/start-devcard-ui!)
