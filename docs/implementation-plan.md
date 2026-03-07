# Implementation Plan: PKM Notes Features

**Origin:** Migrated from AI-dev-Platform (2026-02). Original: `docs/implementation-plan-logseq-interop-and-file-tree.md`.

---

## Features

### 1. Asset Consolidation (DONE)
**Goal:** All app-created files live under a single `pkm-notes/` directory. Notes root stays clean.

**Change:** `local-assets-dir` set to `"pkm-notes/assets"` in `deps/common/src/logseq/common/config.cljs:36`. All asset path resolution uses this constant.

**Before:** `assets/` at graph root (sibling to config folder).
**After:** `pkm-notes/assets/` (inside `pkm-notes/`).

### 2. Cross-Tool Interop — Ignore Other Systems (DONE)
**Goal:** Obsidian files invisible in PKM Notes UI.

**Change:** `.obsidian` and `.trash` added to `ignored-path?` in:
- `deps/common/src/logseq/common/graph.cljs` (core/shared)
- `src/main/frontend/components/imports.cljs` (frontend)

**Tests:** `deps/common/test/logseq/common/graph_test.cljs` — confirms `.obsidian`, `.obsidian/foo`, `.trash`, `.trash/deleted.md` are all ignored.

### 3. Read-Only Mode (DROPPED)
Investigated for feasibility. Consolidating under `pkm-notes/` addresses the core need. No further work planned.

### 4. File Tree View (DONE — has build bug)
**Goal:** VS Code / Obsidian-style folder/file explorer in the left sidebar.

**Implementation:**
- `paths->tree` (line 324) — builds nested tree from file paths
- `file-tree-node` (line 338) — recursive tree renderer (**BUG: uses `defn-` instead of `rum/defc`**)
- `sidebar-file-tree` (line 373) — Rum component, fetches files via `db-async/<get-files`, renders tree
- i18n: `:left-side-bar/file-tree` = `"Files"` in `en.edn`
- Wired into `sidebar-container` at line 505
- Expand/collapse persisted via `:ui/navigation-item-collapsed?`

**Data source:** `db-async/<get-files` (defined at `src/main/frontend/db/async.cljs:26`) — Datalog query for all `:file/path` entities.

### 5. Folder Rebrand (DONE)
All on-disk folder names changed from `logseq` to `pkm-notes`:
- `pkm-notes/config.edn`, `pkm-notes/custom.css`, `pkm-notes/custom.js`, `pkm-notes/assets/`
- `~/pkm-notes/graphs/` (graph storage), `~/.pkm-notes/` (app config)
- `pkm-notes_db_` / `pkm-notes_local_` prefixes
- `pkm-notes://` protocol scheme (Electron)
- `.pkm-notes-pool-` OPFS prefix (browser)

### 6. UI Rebrand (PLANNED)
Replace Logseq branding with PKM Notes. Stock navy/light blue palette. See PKM-SESSION.md Phase 1.

---

## Key References

| File | Purpose |
|------|---------|
| `deps/common/src/logseq/common/config.cljs` | `app-name "pkm-notes"`, `local-assets-dir "pkm-notes/assets"` |
| `deps/common/src/logseq/common/graph.cljs` | `ignored-path?` (core) |
| `src/main/frontend/components/imports.cljs` | `ignored-path?` (frontend) |
| `src/main/frontend/components/left_sidebar.cljs` | File tree UI |
| `src/main/frontend/db/async.cljs` | `<get-files` data source |
| `src/resources/dicts/en.edn` | i18n strings |
| `frontend.handler.repo` | Graph creation, config |
| `frontend.handler.assets` | Asset dir management |
| `frontend.config` | Graph root path resolution |
