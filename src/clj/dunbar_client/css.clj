(ns dunbar-client.css
  (:require [garden.core :as g]))

(def stylesheet
  [[:body {:background-color :#dddddd}]
   [:h1 {:font-weight "normal"}]
   ])




(g/css {:output-to "resources/public/css/garden.css"
        :pretty-print? true} stylesheet)
(g/css {:output-to "resources/public/css/garden.min.css"
        :pretty-print? false} stylesheet)
