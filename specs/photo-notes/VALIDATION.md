# Validation — photo-notes

Spec: `specs/photo-notes/SPEC.md` · Examples: `EXAMPLES.feature` ·
Waves: `WAVES.md` · Gates: `GATES.md`

## Traceability

| AC | Criterion | Scenario(s) | Wave(s) | Test evidence |
|----|-----------|-------------|---------|---------------|
| 1 | Saving the edit screen twice never duplicates note rows for that photo | Save is idempotent | @w1 | — none (Room behaviour, JVM-untestable); verified by build only — pending **G1** |
| 2 | Existing notes load and render as markers without racing user taps | Notes load before interaction | @w1 | — none (threading + Room read); verified by build only — pending **G1** |
| 3 | Tapping a marker shows its text and offers edit/delete | Manage a note | @w2 | — none (touch dispatch + dialog); verified by build only — pending **G1** |
| 4 | Editing a note updates its text and survives reload | Edit persists | @w2 | — none (Room update + reload); verified by build only — pending **G1** |
| 5 | Deleting a note removes the marker and the DB row | Delete persists | @w2 | — none (Room delete + marker removal); verified by build only — pending **G1** |
| 6 | Submitting an empty note is rejected | Empty note rejected | @w3 | — none (UI validation only; existing JVM tests do not exercise `EditPictureActivity`); verified by build only — pending **G1** |
| 7 | `sh gradlew test --no-daemon` + `assembleDebug` pass on JDK 17 | (build gate — verified per wave) | — | clean `test` + `assembleDebug` BUILD SUCCESSFUL locally on JDK 17 per wave; CI Android unit tests green on PRs #7, #8, #9 |

## Scenario coverage

- Scenarios: 6 total — **0 with automated test evidence**, 6 verified by
  per-wave build (`test` + `assembleDebug`) and CI, all pending human
  gate **G1**.
- ✗ `Save is idempotent` (@w1) — **no automated evidence.** The
  delete-by-fileName + re-insert rewrite in `persistData()`/`DaoAccess`
  is Room SQL — JVM unit tests cannot exercise it without Robolectric
  (not a dependency). Pending **G1**.
- ✗ `Notes load before interaction` (@w1) — **no automated evidence.**
  DB-read threading and marker render ordering in
  `EditPictureActivity` is unverifiable without an emulator. Pending
  **G1**.
- ✗ `Manage a note` (@w2) — **no automated evidence.** Marker `ImageView`
  tagging + click dispatch + note dialog are pure Android UI. Pending
  **G1**.
- ✗ `Edit persists` (@w2) — **no automated evidence.** Room update
  path via `DaoAccess`. Pending **G1**.
- ✗ `Delete persists` (@w2) — **no automated evidence.** Room delete
  path + marker removal. Pending **G1**.
- ✗ `Empty note rejected` (@w3) — **no automated evidence.** The
  trim/non-empty check lives inside `EditPictureActivity`; no unit
  testable seam was added (legacy style, per SPEC decisions). Pending
  **G1**.

## Demo script (manual walkthrough — pending G1)

Gate **G1 — On-device smoke test** (blocking, owner: product-owner,
resolver: human) is the only remaining validation step: all runtime
evidence for @w1–@w3 awaits it. Suggested walkthrough covering every
scenario:

1. Build and install:
   ```bash
   sh gradlew assembleDebug --no-daemon   # JDK 17, SDK 34
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
2. **Notes load before interaction** (@w1): open the app, pick a photo
   in the edit screen. If the photo already has notes (from a prior
   session), markers should appear only after the DB read finishes —
   tapping during the brief load must not corrupt the note list.
3. **Empty note rejected** (@w3): tap the photo and submit an empty or
   whitespace-only comment → no marker is created and a message is shown.
4. **Manage a note** (@w2): add a note ("fix lighting"), then tap its
   marker → dialog shows the text with Edit / Delete / Cancel.
5. **Edit persists** (@w2): edit it to "fix lighting + crop" and confirm
   → reopen the photo (or re-enter the screen) → the marker dialog shows
   the new text.
6. **Delete persists** (@w2): add a second note, delete it via its
   marker dialog → its marker disappears, the other note remains.
7. **Save is idempotent** (@w1): with notes present, confirm/save the
   edit screen twice (or confirm, re-open, confirm again) → reopening
   shows exactly the original note count — no duplicated markers/rows.

Record the result on gate G1 in `GATES.md`/`LOOP.md` (oracle-owned — do
not hand-edit `LOOP.md`; `ship_loop` records the waiver/evidence).

## Gaps

- **All runtime evidence is pending human gate G1.** Every scenario in
  this feature is Room persistence and Android touch/dialog behaviour.
  `sh gradlew test` runs JVM-only unit tests with no Robolectric and CI
  has no emulator, so *no automated check* covers any scenario — this
  is honest, not an oversight. The feature is **not** fully validated
  until G1 is walked on a device/emulator.
- **Note UUIDs regenerate on save** (SPEC decision: delete-by-fileName +
  re-insert) — internal-only identifiers, acceptable per spec but
  worth knowing if markers ever need stable identity.
- ship_validate reported "no test evidence" for all scenarios including
  a weak C#-test token match on AC6 — heuristic artifact from sibling
  repos, not applicable here; disregarded.
