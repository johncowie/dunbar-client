(ns dunbar-client.view
  (:require [reagent.core :refer [atom cursor]]
            [dunbar-client.model :as model]
            [traversy.lens :as l]
            [dunbar-client.traversy :refer [traversy-cursor]]))

(defn input-field [label-text]
  (fn [value]
    [:div
      [:label label-text]
      [:input {:type "text"
               :class (if (empty? @value) "error" "")
               :value @value
               :on-change #(reset! value (-> % .-target .-value))}]]))


;; Declare atom with all data here, and then use cursors

(defn friend-form [submit-function]
  (fn [friend]
    [:div
       [:h1 "Add a friend " (l/view-single {:a 1} (l/in [:a]))]
       [:form
          [:fieldset
            [(input-field "First name: ") (traversy-cursor friend (l/in model/friend->first-name))]
            [(input-field "Last name: ") (cursor friend model/friend->last-name)]]
          [:fieldset
            [(input-field "Date of birth: ") (cursor friend model/friend->dob)]
           ]
        [:input {:type "button" :value "Create" :on-click submit-function}]
        ]]))

(defn friends-list [friends]
  [:ul
    (for [f @friends]
       ^{:key (:id f)} [:li (:first-name f) " " (:last-name f)])])
