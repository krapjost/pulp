(ns app.ui.write
  (:require
   [com.fulcrologic.fulcro.components :refer [defsc factory get-query
                                              get-state set-state! transact!]]
   [com.fulcrologic.fulcro.dom :refer [select option form textarea div label input select div h2 ul li p h3 button b img span a]]))

(defsc Write [this props]
  {:query         [:write/welcome-message]
   :initial-state {:write/welcome-message "Hi! Welcome"}
   :ident         (fn [] [:component/id :write])
   :route-segment ["write"]}
  (div :.ui.segment
       (div :.ui.fluid.search.selection.dropdown
            (input {:type "hidden" :name "country"})
            (div :.default.text "Select")
            (div :.menu
                 (div :.item "one")
                 (div :.item "two")
                 (div :.item "trhe")))
       (div :.ui.form
            (div :.field
                 (label "Title")
                 (input {:type "text"}))
            (div :.field
                 (label "Description")
                 (textarea {:rows 2}))

            (div :.field
                 (label "Genre")
                 (select :.ui.fluid.search.dropdown {:multiple ""}
                         (option {:value "ff"} "Fantasy")
                         (option {:value "sf"} "Si-Fi")
                         (option {:value "cl"} "Classic")
                         (option {:value "pm"} "Poem")
                         (option {:value "hb"} "Hardboiled")
                         (option {:value "mt"} "Mystery")
                         (option {:value "tr"} "Thriller")))
            (div :.field
                 (label "Content")
                 (textarea))
            (div :.ui.divider)
            (div :.ui.submit.button "Write"))))
