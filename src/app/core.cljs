(ns app.core
  (:require
    [devcards.core])
  (:require-macros
    [cljs.test :refer [is testing]]
    [devcards.core :refer [defcard deftest dom-node]]))

(defonce state (atom {:initial "state"}))

(def app-elem (js/document.querySelector "#app"))

(set! (.-innerHTML app-elem)
      (str "<h1>figwheel.main</h1>"
           "<code>" (pr-str @state) "</code>"))


(defcard app-card
  "Example card"
  [:code (pr-str state)])
