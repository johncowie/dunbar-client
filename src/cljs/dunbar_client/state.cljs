(ns dunbar-client.state
  (:require [reagent.core :refer [atom cursor]]))

(defonce state
  (atom
    {:friends []
     :next-id 0}))

(defn next-id []
  (cursor state [:next-id]))

(defn inc-next-id! []
  (swap! (next-id) inc))

(defn friends []
  (cursor state [:friends]))

(defn add-id! [friend]
  (assoc friend :id (inc-next-id!)))

(defn add-friend! [friend]
  (swap! (friends) #(conj % (add-id! friend))))
