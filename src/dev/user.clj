(ns user
  (:require
    [clojure.tools.namespace.repl :as tools-ns :refer [set-refresh-dirs]]
    [expound.alpha :as expound]
    [development :refer [restart]]
    [clojure.spec.alpha :as s]))

(set-refresh-dirs "src/main" "src/dev" "src/test")
(alter-var-root #'s/*explain-out* (constantly expound/printer))

;; NOTE: To start working with server: Require development.clj, and use start there.
;; This leads to faster and more reliable REPL startup
;; in cases where your app is busted.

(comment
  (require 'development)
  (in-ns 'development)
  (restart)
  )
