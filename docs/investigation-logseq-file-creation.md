# Investigation: PKM Notes file creation when opening a graph

**Origin:** Migrated from AI-dev-Platform (2026-02). Original: `docs/investigation-report-logseq-file-creation-on-graph-open.md`.

---

## Executive Summary

- **Graph root is never a user-chosen folder.** It is always `~/pkm-notes/graphs/<graph-name>`.
- There is no "open folder as root" flow. "Create new graph" asks for a **name** and creates storage at `~/pkm-notes/graphs/<name>`.
- **Files created under graph root:** `pkm-notes/` (config.edn, custom.css, custom.js) and `pkm-notes/assets/`.
- Global config and plugin config live in the **app config dir** (`~/.pkm-notes/`), not under the graph.
- **Import** reads a source folder but stores in a new graph at `~/pkm-notes/graphs/<new-name>` — source folder is never written to.

## Write Points Under Graph Root

| Code Path | Creates | Condition |
|-----------|---------|-----------|
| `assets-handler/ensure-assets-dir!` | `pkm-notes/assets/` (mkdir) | On `:graph/restored` |
| `db-editor-handler/save-file!` | `pkm-notes/config.edn` | When repo config is saved |
| Editor / export | `pkm-notes/custom.css`, `pkm-notes/custom.js` | When user saves those files |
| `fs/write-file!` (generic) | Various under `pkm-notes/` or `pkm-notes/assets/` | Asset uploads, etc. |

## Key Code Locations

- Config / repo dir: `frontend.config` (`get-repo-dir`, `get-local-dir`)
- Graph creation: `frontend.handler.repo` (`create-db`, `new-db!`)
- Events: `frontend.handler.events` (`:graph/restored` triggers `ensure-assets-dir!`)
- Writes: `frontend.handler.assets`, `frontend.handler.config`, `frontend.handler.db_based.editor`, `frontend.fs`
- Electron: `src/electron/electron/handler.cljs` (`open-dir-dialog`)

## Resolution

Assets consolidated under `pkm-notes/` folder. Notes root contains only user files and one `pkm-notes/` directory. This is the same pattern other tools use (Obsidian creates `.obsidian/`).
