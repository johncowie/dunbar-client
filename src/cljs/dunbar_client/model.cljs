(ns dunbar-client.model
  (:require [traversy.lens :as l]))

(defn new-friend [id first-name last-name dob]
  {:id id
   :first-name first-name
   :last-name last-name
   :dob dob
   :comments ""
   :birthday-ideas ""})

(defn add-birthday-ideas [friend birthday-ideas]
  (assoc friend :birthday-ideas birthday-ideas))

(defn add-comments [friend comments]
  (assoc friend :comments comments))

(defn blank-friend []
  (new-friend nil "" "" ""))

(def friend->first-name (l/in [:first-name]))
(def friend->last-name (l/in [:last-name]))
(def friend->dob (l/in [:dob]))
(def friend->comments (l/in [:comments]))
(def friend->birthday-ideas (l/in [:birthday-ideas]))
