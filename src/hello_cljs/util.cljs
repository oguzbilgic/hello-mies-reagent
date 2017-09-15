(ns hello-cljs.util
  (:require [reagent.core :as r]
            [ajax.core :refer [POST]]
            [clojure.walk :refer [keywordize-keys]]))

(defn set-state [state new-state]
  (reset! state (merge @state new-state)))

(defn with-state [initial-state component]
  (fn [props children]
    (let [state (r/atom (initial-state props))]
      (fn []
        (component props children state)))))

(defn with-query [query component]
  (fn [props children]
    (let [data (r/atom nil)]
      (fn []
        (when (nil? @data)
          (POST "https://api.graph.cool/simple/v1/cj3janzsw53tk0170pnnjuzmg"
            {:params {:query query}
             :format :json
             :response-format :json
             :handler (fn [response]
                        (reset! data (:data (keywordize-keys response))))}))
        (if (nil? @data)
          nil
          (component (merge props @data) children))))))
