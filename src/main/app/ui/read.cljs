(ns app.ui.read
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div input h2 ul li p h3 button b img span a]]
   ))

(defsc BookCard
  [_ {:book/keys [name author description image]}]
  {:query         [:book/name :book/author :book/description :book/image]
   :ident         (fn [] [:component/id :book-card])}
  (div :.column
       (div :.ui.fluid.card
            (div :.image
                 (img {:src image}))
            (div :.content
                 (div :.header name)
                 (div :.meta author)
                 (div :.description description)))))
(def ui-book-card (comp/factory BookCard {:keyfn :book/name}))

(defsc BookCardList
  [_ {:list/keys [label book]}]
  (div
   (h2 label)
   (div :.ui.four.column.grid
        (map ui-book-card book))))
(def ui-book-card-list (comp/factory BookCardList))


(defsc Read
  [_ props]
  {:query         [:read/all]
   :ident         (fn [] [:component/id :read])
   :initial-state {:read/all {}}
   :route-segment ["read"]}
  (let [{book-map :read/all} props]
    (div :.ui.container
         (ui-book-card-list (:fantasy book-map))
         (ui-book-card-list (:dark book-map))
         (ui-book-card-list (:hard-sf book-map))
         (ui-book-card-list (:myth book-map)))))
