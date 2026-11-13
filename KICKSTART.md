# Kickstart Prompt — pkm-notes

Last updated: 2026-11-13
Base commit: 068af085cb60bb28d56f6c08287dc0cbff6be432
Maintained by `scripts/update_kickstart.py` — do not edit manually.

---

## Session bootstrap — /kickstart reads and executes this automatically:

```
Repo: pkm-notes — continuing from previous session.

## Last completed (2026-11-13)
M4 CLAUDE.md consolidation done (PR merged). pkm-notes is a Logseq fork rebranded. All on-disk paths rebranded from logseq to pkm-notes (2026-03-13). REFRESHED 2026-11-13 (handoff bookkeeping only; no session worked this repo): the handoff had gone stale against main. Commits that landed since it was last written, taken from the commit log rather than from any session record, so no narrative beyond the subjects is available: 068af08 chore: remove pr-labeler workflow — cosmetic noise on a personal fork (#7);4400290 fix(ci): add continue-on-error to PR Labeler — branch may be deleted before action runs (#6);70cb358 chore: gitignore android/raw/ directory (replaces file-level ignore) (#5);

## Current branch
main

## Open PRs
  (none)

## Start here: No active development. Low priority: verify bb dev:lint-and-test still passes cleanly. Check android/app/src/main/res/raw/ untracked dir.

- Run: bb dev:lint-and-test
- Inspect android/app/src/main/res/raw/ — add to .gitignore if it is a build artifact

## Persistent context
- Complex ClojureScript Electron+Capacitor app. Pre-existing compile error in left_sidebar.cljs — do not fix without explicit instruction. Use yarn not npm. SSH not configured — HTTPS only.

Do not ask about optional parameters. Start working.
```
