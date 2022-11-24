(ns app.ui.util
  (:require
   [taoensso.timbre :as log]

   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]))

(defn active-when [app route]
  (log/spy :info "route is" (some-> (dr/current-route app) first))

  (when (= route (some-> (dr/current-route app) first))
    "active"))

(defn go-> [app route]
  (fn [] (dr/change-route app [route])))
