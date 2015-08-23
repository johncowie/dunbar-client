(ns dunbar-client.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [dunbar-client.view :as view]
              [dunbar-client.model :as model]
              [dunbar-client.state :as state]
              [dunbar-client.api :as api])
    (:import goog.History))

(def history (History. ))
(def api (api/new-stub-api))

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

(defn create-friend-page []
  (let [new-friend (atom (model/blank-friend))]
    [:div
     [(view/friend-form #(state/add-friend! @new-friend)) new-friend]
     [view/friends-list (state/friends)]]))

(defn edit-friend-page [id]
  (fn []
    (let [friend (state/friend-by-id id)]
      [:div
        [(view/friend-form (constantly nil)) friend]
        [view/friends-list (state/friends)]
       ])))


(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

;; Could replace references to session with our own state namespace
(secretary/defroute "/" []
  (session/put! :current-page create-friend-page))

(secretary/defroute "/friends" []
  (session/put! :current-page friends-page))

(secretary/defroute "/friends/:id" [id]
  (session/put! :current-page (edit-friend-page (js/parseInt id))))


;; -------------------------
;; Initialize app
(defn mount-root []
  (state/init-state! (api/load-friends api))
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
