(ns app.core
  (:require [cljs.pprint]))

(defonce state (atom {:initial "state"}))

(def app-elem (js/document.querySelector "#app"))

(set! (.-innerHTML app-elem)
      (str "<h1>figwheel.main</h1>"
           "<code>" (pr-str @state) "</code>"))
