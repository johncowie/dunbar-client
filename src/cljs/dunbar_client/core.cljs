(ns dunbar-client.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [dunbar-client.view :as view]
              [dunbar-client.model :as model]
              [dunbar-client.state :as state])
    (:import goog.History))



(def history (History. ))

(defn nav! [token]
  (.setToken history token))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto history
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))


;; -------------------------
;; Views


(defn friends-page []
  [view/friends-list (state/friends)])

(defn home-page []
  (let [new-friend (atom (model/blank-friend))]
    [:div
     [(view/friend-form #(do (state/add-friend! @new-friend)
                             ;(nav! "/friends")
                           )) new-friend]
     [view/friends-list (state/friends)]]))


(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page home-page))

(secretary/defroute "/friends" []
  (session/put! :current-page friends-page))


;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
