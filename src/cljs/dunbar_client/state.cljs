(ns dunbar-client.state
  (:require [reagent.core :refer [atom cursor]]
            [traversy.lens :as l]
            [dunbar-client.traversy :refer [traversy-cursor]]))

(defonce state
  (atom
    {:friends []
     :next-id 0}))

(defn- next-id []
  (cursor state [:next-id]))

(defn- inc-next-id! []
  (swap! (next-id) inc))

(defn- add-id! [friend]
  (assoc friend :id (inc-next-id!)))

(defn friends []
  (cursor state [:friends]))

(defn- matches-id? [id]
  (fn [f]
    (= (:id f) id)))

(defn friends->friend [id]
  (l/*> (l/in [:friends]) l/each (l/conditionally (matches-id? id))))

(defn friend-by-id [id]
  (traversy-cursor state (friends->friend id)))

(defn copy [a]
  (atom @a))

(defn add-friend! [friend]
  (swap! (friends) #(conj % (add-id! friend))))

(defn init-state! [friend-list]
  (let [highest-id (->> friend-list (map :id) sort last)]
    (reset! (friends) friend-list)
    (reset! (next-id) highest-id)))
