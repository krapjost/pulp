(ns app.model.book
  (:require
   [app.model.mock-database :as db]
   [datascript.core :as d]
   [com.fulcrologic.guardrails.core :refer [>defn => | ?]]
   [com.wsscode.pathom.connect :as pc :refer [defresolver defmutation]]
   [taoensso.timbre :as log]
   [clojure.spec.alpha :as s]))

(>defn all-book-ids
       "Returns a sequence of UUIDs for all of the active accounts in the system"
       [db]
       [any?
        => (s/coll-of uuid? :kind vector?)]
       (d/q '[:find [?v ...]
              :where
              [?e :book/id ?v]] db))

(>defn get-book
       "Return one book"
       [db id subquery]
       [any? uuid? vector?
        => (? map?)]
       (d/pull db subquery [:book/id id]))

(defresolver all-books-resolver
  [{:keys [db]} input]
  {::pc/output [{:all-books [:book/id]}]}
  {:all-books (mapv
               (fn [id] {:book/id id})
               (all-book-ids db))})

(defresolver book-resolver
  [{:keys [db]} {:book/keys [id]}]
  {::pc/input #{:book/id}
   ::pc/output [:book/name :book/author]}
  (get-book db id [:book/name :book/author]))

(def resolvers [book-resolver all-books-resolver])
