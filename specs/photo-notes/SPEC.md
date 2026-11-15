# Spec: photo-notes

> Spec-driven development artifact. This file is the contract: implementation
> is done when every acceptance criterion maps to a scenario in
> `EXAMPLES.feature` and every scenario has a passing check.

## Problem

`EditPictureActivity` lets users pin a text "note" on a photo; notes persist
in Room (`Note` entity + `DaoAccess`). The feature is half-wired and buggy:

- **Duplicate writes:** `persistData()` calls `insertMultipleMovies(notes)`
  with the *entire* loaded list — every save re-inserts already-persisted
  notes, so annotations multiply in the DB.
- **No management:** markers are anonymous `ImageView`s with no click
  handling — a note can be created but never viewed, edited, or deleted.
- **Threading hazard:** notes are fetched on a raw `Thread` that reassigns
  the shared `notes` list while the UI thread may already be reading it.
- **Silent data loss on text:** an empty note text is accepted.

## Users

- App users annotating their photos with pinned, persistent notes.

## Scope (in)

- **Idempotent persistence:** replace insert-all with delete-by-fileName +
  insert (or equivalent upsert) so saving never duplicates rows; add the
  needed `@Query` to `DaoAccess`.
- **Thread safety:** note list mutations happen on one thread; DB reads
  post results back to the main thread before touching views.
- **Note management:** tapping a marker opens a dialog showing the note
  text with Edit / Delete / Cancel; edit updates text + DB; delete removes
  marker + DB row.
- **Validation:** empty/blank note text is rejected with a message.
- Marker views tracked by note UUID so CRUD can find them.

## Scope (out)

- Camera capture pipeline (`Camera2Fragment`), filter engine,
  gallery grid changes, note export/sharing, note categories/colors,
  schema migrations beyond what `fallbackToDestructiveMigration` already
  does, architecture refactors.

## Acceptance criteria

| # | Criterion | Scenario(s) |
|---|-----------|-------------|
| 1 | Saving the edit screen twice never duplicates note rows for that photo | Save is idempotent @w1 |
| 2 | Existing notes load and render as markers without racing user taps | Notes load before interaction @w1 |
| 3 | Tapping a marker shows its text and offers edit/delete | Manage a note @w2 |
| 4 | Editing a note updates its text and survives reload | Edit persists @w2 |
| 5 | Deleting a note removes the marker and the DB row | Delete persists @w2 |
| 6 | Submitting an empty note is rejected | Empty note rejected @w3 |
| 7 | `sh gradlew test --no-daemon` + `assembleDebug` pass on JDK 17 | (build gate per wave) |

## Decisions

- **Delete-by-fileName + re-insert** chosen over per-note upserts: the DAO
  is tiny, the list is small, and it makes "save" trivially idempotent.
  Trade-off: note UUIDs are regenerated on each save — acceptable because
  UUIDs are internal only (never referenced elsewhere).
- **Marker = tagged ImageView:** each marker `ImageView` gets
  `setTag(note.getUUID())`; a single click listener on the root
  `RelativeLayout` dispatches to the note dialog. No new widgets.
- Room stays the store (no repository abstraction) — legacy style, KISS;
  the seam for testability is the DAO interface itself.
