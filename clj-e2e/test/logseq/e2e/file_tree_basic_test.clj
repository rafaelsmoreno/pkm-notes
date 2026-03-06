(ns logseq.e2e.file-tree-basic-test
  "E2E tests for the left-sidebar file tree (Phase 5 of Logseq implementation plan)."
  (:require
   [clojure.test :refer [deftest testing is use-fixtures]]
   [logseq.e2e.assert :as assert]
   [logseq.e2e.fixtures :as fixtures]
   [logseq.e2e.page :as p]
   [wally.main :as w]))

(use-fixtures :once fixtures/open-page)
(use-fixtures :each fixtures/new-logseq-page fixtures/validate-graph)

(deftest file-tree-section-visible
  (testing "File tree section is present and visible in left sidebar"
    ;; The file tree renders inside #left-sidebar with class .file-tree.
    ;; Ensure the sidebar is open and the file tree section exists.
    (assert/assert-is-visible ".file-tree"))
  (testing "File tree shows 'Files' header"
    (assert/assert-is-visible ".file-tree:has-text('Files')")))

(deftest file-tree-click-opens-file
  (testing "When files exist in tree, clicking a file navigates to file view"
    ;; Wait for file list to load (async from db-async/<get-files)
    (w/wait-for ".file-tree-root, .file-tree-file" {:timeout 5000})
    ;; Demo graph may have files; if so, click first file and verify navigation
    (when (w/visible? ".file-tree .file-tree-file")
      (w/click (.first (w/-query ".file-tree .file-tree-file")))
      (w/wait-for "div[data-testid='page title'], .file-content" {:timeout 3000})
      (is (or (w/visible? "div[data-testid='page title']")
              (w/visible? ".file-content"))
          "After clicking file in tree, page title or file content should be visible")))
