(ns logseq.common.graph-test
  (:require [logseq.common.graph :as common-graph]
            [cljs.test :refer [deftest is use-fixtures async]]
            ["fs" :as fs]
            ["path" :as node-path]))

(use-fixtures
  :each
  ;; Cleaning tmp/ before leaves last tmp/ after a test run for dev and debugging
  {:before
   #(async done
           (if (fs/existsSync "tmp")
             (fs/rm "tmp" #js {:recursive true} (fn [err]
                                                  (when err (js/console.log err))
                                                  (done)))
             (done)))})

(defn- create-logseq-graph
  "Creates a minimal mock graph"
  [dir]
  (fs/mkdirSync (node-path/join dir "pkm-notes") #js {:recursive true})
  (fs/mkdirSync (node-path/join dir "journals"))
  (fs/mkdirSync (node-path/join dir "pages")))

(deftest get-files
  (create-logseq-graph "tmp/test-graph")
  ;; Create files that are recognized
  (fs/writeFileSync "tmp/test-graph/pages/foo.md" "")
  (fs/writeFileSync "tmp/test-graph/journals/2023_05_09.md" "")
  ;; Create files that are ignored
  (fs/mkdirSync (node-path/join "tmp/test-graph" "pkm-notes" "bak"))
  (fs/writeFileSync "tmp/test-graph/pkm-notes/bak/baz.md" "")
  (fs/writeFileSync "tmp/test-graph/pkm-notes/.gitignore" "")
  (is (= ["tmp/test-graph/journals/2023_05_09.md" "tmp/test-graph/pages/foo.md"]
         (common-graph/get-files "tmp/test-graph"))))

(deftest ignored-path-obsidian-and-trash
  "Phase 3 (ignore other systems): .obsidian and .trash must be ignored"
  (let [graph-dir "/tmp/graph"]
    ;; Paths must start with graph-dir for trim-dir-prefix; rpath becomes .obsidian etc.
    (is (common-graph/ignored-path? graph-dir (str graph-dir "/.obsidian"))
        ".obsidian dir is ignored")
    (is (common-graph/ignored-path? graph-dir (str graph-dir "/.obsidian/foo"))
        ".obsidian/foo is ignored")
    (is (common-graph/ignored-path? graph-dir (str graph-dir "/.obsidian/.trash/bar"))
        ".obsidian/.trash nested is ignored")
    (is (common-graph/ignored-path? graph-dir (str graph-dir "/.trash"))
        ".trash dir is ignored")
    (is (common-graph/ignored-path? graph-dir (str graph-dir "/.trash/deleted.md"))
        ".trash/deleted.md is ignored")
    (is (not (common-graph/ignored-path? graph-dir (str graph-dir "/pages/note.md")))
        "Regular pages path is not ignored")))