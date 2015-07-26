(ns dunbar-client.model)

(defn new-friend [first-name last-name dob]
  {:first-name first-name
   :last-name last-name
   :dob dob})

(defn blank-friend []
  (new-friend "" "" ""))

(def friend->first-name [:first-name])
(def friend->last-name [:last-name])
(def friend->dob [:dob])
