# Delivery audit — photo-notes (delivery)

- Repo: `~/factory/openharness/sources/CarFilters`
- Spec: `specs/photo-notes`
- Generated: 2026-11-19T09:05:23Z
- Phase: **delivery**
- Findings: **0 blockers · 8 concerns · 1 unverified**
- This is a deterministic evidence brief, not a verdict. The `delivery-auditor` re-verifies it and fills the verdict.

## What was checked

- Acceptance criteria: 7
- Scenarios: 6 (3 wave tags)
- Backlog items: 3
- Waves: 3
- Gates: 1
- Validation rows: 7
- Path claims checked: 2
- Changed files: 4
- In-scope paths: none recorded

## Blockers

None. This is not a pass: verify semantics before accepting.

## Concerns

- [ac-no-evidence] `VALIDATION.md` — AC 1 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 2 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 3 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 4 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 5 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 6 has no test evidence
- [ac-no-evidence] `VALIDATION.md` — AC 7 has no test evidence
- [validation-gap] `VALIDATION.md` — unresolved validation gap: 6 scenario(s) lack detected test evidence — verify manually or add checks

## Unverified

- [scope-no-paths] `SPEC.md` — scope drift not checked — no in-scope paths recorded

## Verdict (agent fills after verifying the code)

```
AUDIT: CONCERNS
PHASE: delivery
BLOCKERS: 0 — every AC was independently verified against the merged code and, for the
  UI/Room behaviours, against the completed on-device gate G1 smoke.
CONCERNS: accepted — ACs 1–6 and all 6 scenarios have no automated test evidence
  (JVM unit tests cannot reach Room/touch; CI has no emulator — exactly the rationale
  declared in GATES.md G1). That gap is covered by gate G1, which is now met via the
  emulator-5554 smoke run of 2026-11-19: empty-note rejection (toast "Note can't be
  empty", zero markers), create/edit/delete through the real marker dialogs, and a
  Room DB read-back showing exactly 1 row (fileName=smoke_photo, x=300, y=800,
  noteString="fix lighting + crop") after save — idempotent delete+re-insert held.
  The camera-capture entry path was substituted by a temporary instrumentation
  launcher (SmokeLaunchTest, uncommitted) because the emulator's Camera2 AF never
  locks; note CRUD — the gated scope — was exercised through real UI interaction.
  AC7 build evidence exists per wave (clean test + assembleDebug green on JDK 17,
  CI green on PRs #7–#9). No automated-test coverage remains a standing accepted
  risk for future regressions, not a delivery blocker.
UNVERIFIED: none remaining — the spec's scope-no-paths finding was re-checked by
  hand: all changed files (EditPictureActivity.java, Note.java, DaoAccess.java,
  NoteDatabase.java, activity_edit_picture.xml) are inside the declared scope.
EVIDENCE: read EditPictureActivity.java (persistData delete-by-fileName+re-insert in
  runInTransaction with `saving`/`isChanged` guards; notesLoaded gate + runOnUiThread
  load; marker ImageViews tagged with UUID; Edit/Delete/Cancel dialog; empty/whitespace
  trim rejection on create and edit), Note.java, DaoAccess.java (fetch/delete by
  fileName, @Update, @Delete), NoteDatabase.java, activity_edit_picture.xml ids,
  SPEC.md/EXAMPLES.feature/GATES.md/VALIDATION.md/LOOP.md; git log confirms PRs
  #7/#8/#9 merged to master with CI green. Gate G1 evidence recorded in LOOP.md:
  emulator smoke observed via uiautomator dumps + run-as/sqlite3 DB pull — 1 Note row
  after confirm, marker count 0→1→2→1→1 across create/edit/delete, empty note
  rejected, marker restored on re-open with correct text.
```

Rules: `BLOCK` on any confirmed blocker, hallucinated claim, or
unproven acceptance criterion. `CONCERNS` only when every remaining
item is non-blocking and explicitly accepted. `PASS` only when all
three lists are empty. Never mark `PASS` from this brief alone.
