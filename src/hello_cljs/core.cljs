(ns hello-cljs.core
  (:require [reagent.core :as r]
            [hello-cljs.util :refer [set-state with-state with-query]]))

(defn initial-state [props]
  {:hover false})

(defn handle-change [state props event]
  (set-state state {:text (-> event .-target .-value)}))

(defn handle-select-change [state props event]
  (set-state state {:text (-> event .-target .-value)}))

(defn background-color [state props]
  (cond
    (= "green" (:text state)) "green"
    (= "yellow" (:text state)) "yellow"
    (= "pink" (:text state)) "pink"
    (:hover state) (:color props)
    :else "red"))

(defn footer-render [props children state]
  [:div {:style {:backgroundColor (background-color @state props)
                 :padding 10}
         :on-mouse-enter #(set-state state {:hover true})
         :on-mouse-leave #(set-state state {:hover false})}
   [:input {:type "text"
            :value (:text @state)
            :on-change #(handle-change state props %)}]
   [:select {:on-change #(handle-select-change state props %)}
    [:option nil]
    [:option "green"]
    [:option "pink"]
    [:option "yellow"]]
   [:ul
    (for [classroom (:allClassrooms props)]
      [:li {:key (:id classroom)}
       (:name classroom)
       " - "
       (:id classroom)])]
   [:div {:style {:background-color "white"}}
    children]
   @state])

(def footer-state (with-state initial-state footer-render))

(def footer
  (with-query
    "{
      allClassrooms {
        id
        name
      }
    }" 
    footer-state))

(defn header [props children]
  [:div
   "this is header"
   [:button {:on-click (fn [] (js/alert "clicked"))}
     "click me"]])

(defn app []
  [:div
   [header]
   "hello"
   [footer {:color "blue"}
    [:div
     "this is children"]]])

(r/render [app] js/document.body)
