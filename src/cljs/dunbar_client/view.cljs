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

(defn text-area [label-text]
  (fn [value]
    [:div
      [:label label-text]
      [:input {:value @value
               :on-change #(reset! value (-> % .-target .-value))}]]))


;; Declare atom with all data here, and then use cursors

(defn friend-form [submit-function]
  (fn [friend]
    [:div
       [:h1 "Add a friend "]
       [:form
          [:fieldset
            [(input-field "First name: ")
             (traversy-cursor friend model/friend->first-name)]
            [(input-field "Last name: ")
             (traversy-cursor friend model/friend->last-name)]]
          [:fieldset
            [(input-field "Date of birth: ")
             (traversy-cursor friend model/friend->dob)]]
          [:fieldset
            [(input-field "Comments: ")
             (traversy-cursor friend model/friend->comments)]]
          [:fieldset
            [(input-field "Birthday ideas: ")
             (traversy-cursor friend model/friend->birthday-ideas)]
           ]
        [:input {:type "button" :value "Create" :on-click submit-function}]
        ]]))

(defn friend-list-item [f]
  [:li (str (:id f) " " (:first-name f) " " (:last-name f))
       ])

(defn friends-list [friends]
  [:ul
    (for [f @friends]
       ^{:key (:id f)} (friend-list-item f))])
