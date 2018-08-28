(ns app.cards
  (:require
    [devcards.core]
    [app.core :as app])
  (:require-macros
    [cljs.test :refer [is testing]]
    [devcards.core :refer [defcard deftest dom-node]]))

(devcards.core/start-devcard-ui!)
