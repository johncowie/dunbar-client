(ns dunbar-client.prod
  (:require [dunbar-client.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
