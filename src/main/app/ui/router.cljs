(ns app.ui.router
  (:require
   [app.ui.read :refer [Read]]
   [app.ui.write :refer [Write]]
   [app.ui.session :refer [Login Signup SignupSuccess]]

   [com.fulcrologic.fulcro.dom :refer [div]]
   [com.fulcrologic.fulcro.components :refer [factory computed get-computed]]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]))

(dr/defrouter RootRouter [this {:keys [current-state route-factory route-props]}]
  {:router-targets      [Read
                         Write
                         Login
                         Signup
                         SignupSuccess]
   :always-render-body? true}
  (div :.container
       (when-not (= :routed current-state)
         (div :.ui.active.inverted.dimmer
              (div :.ui.loader)))
       (when route-factory
         (route-factory (computed route-props (get-computed this))))))

(def ui-root-router (factory RootRouter))

