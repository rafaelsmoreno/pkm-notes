@.claude/NEXT_SESSION.md

# pkm-notes — Project Rules

## Start Here

- Read `KICKSTART.md` (repo root) for the previous session's next steps.
- Read `.claude/NEXT_SESSION.md` for injected session state (auto-populated above).
- This is a complex ClojureScript codebase — always run `bb dev:lint-and-test` before committing.

## Project

A fork of Logseq, rebranded as pkm-notes. An Electron desktop + mobile (Capacitor) personal knowledge management application built in ClojureScript with shadow-cljs.

This is a **complex, established codebase** — treat every change as potentially impactful to the full application. Read before touching.

## Stack

- **Language:** ClojureScript (CLJS) — NOT JavaScript. Requires Clojure CLI (`clojure`), Java 17+, and Babashka (`bb`).
- **Build system:** shadow-cljs (config: `shadow-cljs.edn`)
- **Runtime:** Electron (desktop), Capacitor (mobile: Android at `android/`, iOS at `ios/`)
- **Package manager:** Yarn (not npm) — use `yarn`, never `npm install`
- **CSS:** Tailwind CSS
- **Test runner:** `bb dev:lint-and-test` (Babashka task)
- **E2E tests:** `clj-e2e/` directory

## Key File Locations

```
src/main/             ← Core application logic (ClojureScript)
  frontend/
    components/       ← UI components
    inference_worker/ ← Text-embedding and vector-search webworker
    worker/           ← Other webworker
      rtc/            ← Real-time collaboration
  mobile/             ← Mobile-specific code
src/electron/         ← Electron desktop code
src/test/             ← Unit tests
deps/                 ← Internal dependencies/modules
clj-e2e/              ← End-to-end tests
shadow-cljs.edn       ← Build config (source paths, targets, nREPL)
bb.edn                ← Babashka tasks
deps.edn              ← Clojure/ClojureScript deps
```

## Common Keywords

All commonly used ClojureScript keywords are defined via `logseq.common.defkeywords/defkeyword`. Search `defkeywords` to find all definitions before adding new ones.

## Dev Commands

```bash
# Lint + unit tests (always run before committing)
bb dev:lint-and-test

# Run a single unit test
bb dev:test -v <namespace/testcase-name>

# Dev watch (ClojureScript hot-reload) — requires clojure compile to succeed first
yarn watch
# or
yarn app-watch

# CSS/assets build
npx gulp build

# ClojureScript compile (has a pre-existing error in left_sidebar.cljs — do not fix without understanding it)
clojure -M:cljs compile app
```

## Known Pre-existing Issues

- `src/main/frontend/components/left_sidebar.cljs` has a Rum mixin syntax error in `defn-` — pre-existing, do not attempt to fix without explicit user instruction
- ClojureScript compile (`clojure -M:cljs compile app`) fails due to the above — this is expected; use `bb dev:lint-and-test` for validation

## Build (Docker alternative)

```bash
make build-pkm-notes-docker
```

## Important Rules

- This is a **Logseq fork** — upstream is `logseq/logseq`. Do not confuse pkm-notes namespace references with the canonical Logseq codebase.
- All on-disk paths have been rebranded from `logseq` to `pkm-notes` (completed 2026-03-13)
- `AGENTS.md` at repo root contains Codex-specific context — do not duplicate there; keep Claude context here
- The `prompts/review.md` file is referenced in AGENTS.md as `@prompts/review.md` — keep it intact

## Repo

- Local: `/projects/pkm-notes/`
- Split from `AI-dev-Platform` monorepo (2026-03-13)
- SSH not configured — use HTTPS for all git operations

## Skills

- `git-governance` — branch naming, commit format, PR rules
- `dev-workflow` — standard branch → commit → review loop
- `pr-review` — structured 15-section PR review
