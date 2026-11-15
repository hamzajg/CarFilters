# Waves — photo-notes

Each wave IS an iteration: a time-boxed, independently shippable
increment — one branch, one PR, green tests, demoable outcome.
Do not start wave N+1 until wave N merges.

## Wave 1 — iteration 1

Scenarios proven by this wave:
- Save is idempotent
- Notes load before interaction

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US1 | As a user, I want saving my annotated photo to never duplicate notes, so that my annotations stay correct | M | — | deleteByFileName query + transactional rewrite of persistData; fix notes-list threading |

## Wave 2 — iteration 2

Scenarios proven by this wave:
- Manage a note
- Edit persists
- Delete persists

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US2 | As a user, I want to view, edit, and delete notes I've placed, so that I can fix mistakes | M | US1 | marker ImageViews tagged by UUID + click dispatch + edit/delete dialogs + DAO update/delete |

## Wave 3 — iteration 3

Scenarios proven by this wave:
- Empty note rejected

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US3 | As a user, I want empty comments rejected, so that stray taps don't create junk notes | S | US2 | trim+nonempty check in addNotesPosition with Toast/message |
