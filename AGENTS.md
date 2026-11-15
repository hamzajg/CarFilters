# AGENTS.md — CarFilters

Guidance for AI agents (and humans) working in this repository.

## Project

**README correction:** despite the name/package (`carfilters`), this is a
**camera + photo app**, not a car app. It captures photos via Camera2,
applies filters in an edit screen, browses a gallery, and stores
photo annotations ("notes" with x/y coordinates) in Room.
Stack: Java 17, Android (AGP 8.7.3 / Gradle 8.9), AndroidX,
compileSdk/targetSdk 34, Room 2.6.1 (`androidx.room`).

## Commands

Verified 2026-11-13 against CI (`.github/workflows/ci.yml`) and build files.

| Task | Command |
|---|---|
| Build APK | `sh gradlew :app:assembleDebug` |
| Unit tests | `sh gradlew test --no-daemon` (CI: JDK 17 + SDK 34) |
| Install | `gradle :app:installDebug` (README; wrapper now checked in) |

Requires: **JDK 17** (AGP 8.x), Android SDK **API 34 +
build-tools 34.0.0**; `local.properties` must set `sdk.dir`. Note: README says "no Gradle wrapper" — **stale**;
`gradlew` is now checked in (added by the wrapper/CI repair).

## Architecture

```
app/src/main/java/com/hamzajg/carfilters/
  MainActivity.java       gallery grid of captured photos + free pictures
  CameraActivity.java     hosts Camera2Fragment
  Camera2Fragment.java    Camera2 API: capture, ImageReader, SurfaceTexture
                          (~1050 lines — the core)
  EditPictureActivity.java photo edit: filters, rotate (filter picker
                          partially commented out) + photo note CRUD:
                          tap-to-place markers, tap-marker dialog
                          (view/edit/delete), idempotent save
                          (delete-by-fileName + re-insert in one
                          transaction), empty-note rejection
  GalleryActivity.java    saved-photos gallery
  AutoFitTextureView.java aspect-fit preview surface
  Note.java               Room @Entity: UUID, fileName, x, y, noteString
  NoteDatabase.java       Room database (8 lines — minimal)
  DaoAccess.java          @Dao: insert/fetch-by-fileName/update/delete Note
app/src/main/res/         layouts (activity_camera, activity_edit_picture,
                          activity_gallery, activity_main, fragment_camera2)
                          + drawables (face_*, free_picture, number*)
app/build.gradle          SDK levels, Room deps (runtime+compiler 1.1.1)
```

### Data model

Room `Note` table: annotation pinned to a photo (`fileName`) at `(x,y)`
with `noteString` text. `DaoAccess` queries notes per fileName — the
"stick a note on a photo" feature. As of `photo-notes` (specs/photo-notes,
PRs #7–#9) the CRUD path is complete end-to-end: markers are `ImageView`s
tagged with the note UUID, tapping one opens a dialog with Edit/Delete/
Cancel, edits and deletes go straight to Room, and saving is idempotent
(delete-by-fileName + re-insert inside `runInTransaction`, re-entry
blocked by a `saving` flag). Empty/whitespace note text is rejected.
Caveat: all runtime behaviour is Room/UI — no automated coverage;
pending human on-device gate **G1** (see specs/photo-notes/GATES.md).

## Conventions

- AndroidX imports (`androidx.*`, `androidx.room`) — post-migration.
- Activities + fragments, no MVVM — `Camera2Fragment` is a plain Fragment
  with CameraDevice/CameraCaptureSession callbacks.
- Java only, support-library era patterns.
- KISS: legacy sample; don't add architecture layers.

## Landmines

- **Default branch is `master`, not `main`.**
- Modernized 2026-11: AGP 3.1.4→8.7.3, Gradle 4.4→8.9, SDK 27→34,
  support libs→AndroidX, Room android.arch→androidx.room 2.6.1,
  JDK 8→17.
- **README misdescribes the app** ("browsing and filtering cars") — the
  code is a camera/photo-filter/annotation app; trust the code.
- README says no wrapper — `gradlew` exists now (wrapper repair merged).
- `EditPictureActivity` has commented-out filter-selection code — filters
  are partially implemented.
- Room is `androidx.room` 2.6.1 (runtime + room-compiler annotationProcessor).
- `Note` uses field `uUID` (odd casing) — match it.
- Photo-note runtime behaviour (save idempotency, marker CRUD, empty-note
  rejection) has **no automated test coverage** — JVM unit tests can't
  reach Room/touch code and CI has no emulator. Human gate G1 in
  `specs/photo-notes/GATES.md` is the only runtime proof; don't claim the
  feature verified until it passes.
- Local clone may sit on `fix/build-wrapper-and-ci` — branch from
  `origin/master`.

## Agent rules

- Branch from `master`; name branches `feat/*`, `fix/*`, `chore/*`, `docs/*`.
- Docs-only changes still go through a PR; never commit to `master` directly.
- Verify before claiming: `sh gradlew test --no-daemon` on JDK 8 + SDK 27 —
  report the real result.
- Keep changes minimal and consistent with surrounding code. No drive-by
  refactors.
- Update AGENTS.md / docs/ARCHITECTURE.md when the app's actual purpose,
  structure, or toolchain changes.
