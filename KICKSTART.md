# Kickstart Prompt — pkm-notes

Last updated: 2026-03-22
Maintained by `scripts/update_kickstart.py` — do not edit manually.

---

## Paste this at the start of the next session:

```
Repo: pkm-notes — continuing from previous session.

## Last completed (2026-03-22)
M4 CLAUDE.md consolidation done (PR merged). pkm-notes is a Logseq fork rebranded. All on-disk paths rebranded from logseq to pkm-notes (2026-03-13).

## Current branch
docs/m6-audit-fixes

## Open PRs
  (none)

## Start here: No active development. Low priority: verify bb dev:lint-and-test still passes cleanly. Check android/app/src/main/res/raw/ untracked dir.

- Run: bb dev:lint-and-test
- Inspect android/app/src/main/res/raw/ — add to .gitignore if it is a build artifact

## Persistent context
- Complex ClojureScript Electron+Capacitor app. Pre-existing compile error in left_sidebar.cljs — do not fix without explicit instruction. Use yarn not npm. SSH not configured — HTTPS only.

Do not ask about optional parameters. Start working.
```
