(ns dunbar-client.api
  (:require [dunbar-client.model :as model]))

(defprotocol Api
  (load-friends [this]))


(def friends
  [(model/new-friend 1 "George" "Harrison" "1/1/2015")
   (model/new-friend 2 "Ringo" "Starr" "1/2/2015")
   (model/new-friend 3 "Paul" "McCartney" "25/3/2015")])

(deftype StubApi []
  Api
  (load-friends [this]
      friends))

(defn new-stub-api []
  (StubApi.))

