(ns app.core
  (:require
    [devcards.core])
  (:require-macros
    [cljs.test :refer [is testing]]
    [devcards.core :refer [defcard deftest dom-node]]))

(defonce state (atom {:initial "state"}))

; Only render our app if we are loaded from the `index.html`
; not the auto-generated devcards page with the #app-cards div.
(when-let [app-elem (js/document.querySelector "#app")]
  (set! (.-innerHTML app-elem)
        (str "<h1>figwheel.main</h1>"
             "<code>" (pr-str @state) "</code>")))

(defcard app-card
  "Example card"
  state)

; Try to change the state from the cljs repl
#_(reset! app.core/state {:new "state 123"})

(deftest state-initialized?
  (testing "happy-path"
    (is (= {:initial "state"} @state))))
