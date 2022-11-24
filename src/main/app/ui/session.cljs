(ns app.ui.session
  (:require
   [app.model.session :as ss]
   [app.ui.util :refer [active-when go->]]
   [com.fulcrologic.fulcro.algorithms.form-state :as fs]
   [com.fulcrologic.fulcro.components :refer [defsc factory get-query
                                              get-state set-state! transact!]]
   [com.fulcrologic.fulcro.dom :as dom :refer [a button div h3 input label p
                                               span]]
   [com.fulcrologic.fulcro.dom.events :as evt]
   [com.fulcrologic.fulcro.mutations :refer [set-string!]]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]
   [taoensso.timbre :as log]))

(defsc Session
  "Session representation.
   Used primarily for server queries.
   On-screen representation happens in Login component."
  [_ {:keys [:session/valid? :account/name]}]
  {:query         [:session/valid?  :account/name]
   :ident         (fn [] [:component/id :session])
   :pre-merge     (fn [{:keys [data-tree]}]
                    (merge {:session/valid? false :account/name ""} data-tree))
   :initial-state {:session/valid? false :account/name ""}})

(defsc LoginButton [this props]
  {:query         [[::uism/asm-id :app.ui.router/RootRouter]
                   {[:component/id :session] (get-query Session)}]
   :initial-state {}
   :ident         (fn [] [:component/id :login-button])}

  (let [current-state (uism/get-active-state this ::ss/session)
        logged-in?    (= :state/logged-in current-state)]
    (div :.right.menu
         (if logged-in?
           [(span :.item
                  (-> (get props [:component/id :session])
                      :account/name))
            (a :.item {:onClick #(uism/trigger! this ::ss/session :event/logout)}
               "Logout")]
           (a :.ui.item
              {:classes [(active-when this "login")]
               :onClick (go-> this "login")}
              "Login")))))

(defn field
  [{:keys [label valid? error-message] :as props}]
  (let [input-props (-> props
                        (assoc :name label)
                        (dissoc :label :valid? :error-message))]
    (div :.ui.field
         (dom/label {:htmlFor label} label)
         (input input-props)
         (div :.ui.error.message {:classes [(when valid? "hidden")]}
              error-message))))

(defsc Login [this {:ui/keys      [error]
                    :account/keys [email]}]
  {:query         [:ui/error
                   :account/email
                   [::uism/asm-id ::ss/session]
                   {[:component/id :session] (get-query Session)}]
   :initial-state {:account/email "" :ui/error ""}
   :route-segment ["login"]
   :ident         (fn [] [:component/id :login])}

  (let [current-state (uism/get-active-state this ::ss/session)
        loading?      (= :state/checking-session current-state)
        password      (or (get-state this :password) "")]
    (div :.ui.segment
         {:onClick (fn [e] (evt/stop-propagation! e))}
         (h3 :.ui.header "Login")
         (div :.ui.form {:classes [(when (seq error) "error")]}
              (field {:label    "Email"
                      :value    email
                      :onChange #(set-string! this :account/email :event %)})
              (field {:label    "Password"
                      :type     "password"
                      :value    password
                      :onChange #(set-state! this {:password (evt/target-value %)})})
              (div :.ui.error.message error)
              (div :.ui.field
                   (button :.ui.button
                           {:onClick (fn []
                                       (uism/trigger! this
                                                      ::ss/session
                                                      :event/login
                                                      {:username email :password password}))
                            :classes [(when loading? "loading")]} "로그인"))
              (div :.ui.message
                   (p "아직 가입 안했어?")
                   (button :.ui.primary.button
                           {:onClick (go-> this "signup")} "가입해"))))))

(defsc Signup
  [this {:account/keys [email password password-again] :as props}]
  {:query             [:account/email :account/password :account/password-again fs/form-config-join]
   :initial-state     (fn [_]
                        (fs/add-form-config Signup
                                            {:account/email          ""
                                             :account/password       ""
                                             :account/password-again ""}))
   :form-fields       #{:account/email :account/password :account/password-again}
   :ident             (fn [] ss/signup-ident)
   :route-segment     ["signup"]
   :componentDidMount (fn [this]
                        (transact! this [(ss/clear-signup-form {})]))}
  (let [submit!  (fn [evt]
                   (when (or (true? evt) (evt/enter-key? evt))
                     (transact! this [(ss/signup! {:email email :password password})])
                     (log/info "Sign up")))
        checked? (fs/checked? props)]
    (div
     (h3 "Signup")
     (div :.ui.form {:classes [(when checked? "error")]}
          (field {:label         "Email"
                  :value         (or email "")
                  :valid?        (ss/valid-email? email)
                  :error-message "Must be an email address"
                  :autoComplete  "off"
                  :onKeyDown     submit!
                  :onChange      #(set-string! this :account/email :event %)})
          (field {:label         "Password"
                  :type          "password"
                  :value         (or password "")
                  :valid?        (ss/valid-password? password)
                  :error-message "Password must be at least 8 characters."
                  :onKeyDown     submit!
                  :autoComplete  "off"
                  :onChange      #(set-string! this :account/password :event %)})
          (field {:label         "Repeat Password" :type "password" :value (or password-again "")
                  :autoComplete  "off"
                  :valid?        (= password password-again)
                  :error-message "Passwords do not match."
                  :onChange      #(set-string! this :account/password-again :event %)})
          (button :.ui.primary.button {:onClick #(submit! true)}
                  "Sign Up")))))

(defsc SignupSuccess [_ _]
  {:query         ['*]
   :initial-state {}
   :ident         (fn [] [:component/id :signup-success])
   :route-segment ["signup-success"]}
  (div
   (h3 "Signup Complete!")
   (p "You can now log in!")))

(def ui-login-button (factory LoginButton))
