(ns app.ui.root
  (:require
   [app.ui.router :refer [RootRouter ui-root-router]]
   [app.ui.session :refer [LoginButton ui-login-button]]
   [app.ui.util :refer [active-when go->]]
   [com.fulcrologic.fulcro.components :refer [defsc get-query]]
   [com.fulcrologic.fulcro.dom :refer [a div]]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]))

(defsc Root [this {:root/keys [root-router login-button]}]
  {:query         [{:root/login-button (get-query LoginButton)}
                   {:root/root-router (get-query RootRouter)}
                   [::uism/asm-id :app.ui.router/RootRouter]]
   :initial-state {:root/login-button {}
                   :root/root-router  {}}}

  (div :.ui.container
       (div :.ui.secondary.pointing.menu
            (a :.item
               {:classes [(active-when this "read")]
                :onClick (go-> this "read")}
               "Read")
            (a :.item
               {:classes [(active-when this "write")]
                :onClick (go-> this "write")}
               "Write")
            (ui-login-button login-button))
       (ui-root-router root-router)))

(comment)
