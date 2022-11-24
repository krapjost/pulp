(ns app.model.book
  (:require
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro.algorithms.data-targeting :as targeting]))

(defn book-path
  "Normalized path to a user entity or field in Fulcro state-map"
  ([id field] [:book/id id field])
  ([id] [:book/id id]))

(defn insert-book*
  "Insert a Book into the correct table of the Fulcro state-map database."
  [state-map {:keys [:book/id] :as book}]
  (assoc-in state-map (book-path id) book))

(defmutation upsert-book
  "Client Mutation: Upsert a book (full-stack. see CLJ version for server-side)."
  [{:keys [:book/id :book/name] :as params}]
  (action [{:keys [state]}]
    (log/info "Upsert book action" state)
    (swap! state (fn [s]
                   (-> s
                     (insert-book* params)
                     (targeting/integrate-ident* [:book/id id] :append [:all-books])))))
  (ok-action [env]
    (log/info "OK action" env))
  (error-action [env]
    (log/info "Error action" env))
  (remote [env]
    (-> env
      (m/returning 'app.ui.root/User)
      (m/with-target (targeting/append-to [:all-books])))))
