# AGENTS.md — CarFilters

Guidance for AI agents (and humans) working in this repository.

## Project

**README correction:** despite the name/package (`carfilters`), this is a
**camera + photo app**, not a car app. It captures photos via Camera2,
applies filters in an edit screen, browses a gallery, and stores
photo annotations ("notes" with x/y coordinates) in Room.
Stack: Java, Android (AGP 3.1.4 / Gradle 4.4), minSdk 21 / compile+target
SDK 27, pre-AndroidX support library, Room 1.1.1 (`android.arch`).

## Commands

Verified 2026-11-13 against CI (`.github/workflows/ci.yml`) and build files.

| Task | Command |
|---|---|
| Build APK | `sh gradlew :app:assembleDebug` |
| Unit tests | `sh gradlew test --no-daemon` (CI: JDK 8 + SDK 27) |
| Install | `gradle :app:installDebug` (README; wrapper now checked in) |

Requires: **JDK 8** (AGP 3.1.4/Gradle 4.4), Android SDK **API 27 +
build-tools 27.0.3**. Note: README says "no Gradle wrapper" — **stale**;
`gradlew` is now checked in (added by the wrapper/CI repair).

## Architecture

```
app/src/main/java/com/hamzajg/carfilters/
  MainActivity.java       gallery grid of captured photos + free pictures
  CameraActivity.java     hosts Camera2Fragment
  Camera2Fragment.java    Camera2 API: capture, ImageReader, SurfaceTexture
                          (~1050 lines — the core)
  EditPictureActivity.java photo edit: filters, rotate (filter picker
                          partially commented out)
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
"stick a note on a photo" feature.

## Conventions

- Pre-AndroidX imports (`android.support.*`, `android.arch.persistence.room`)
  — match them; AndroidX migration is a separate project.
- Activities + fragments, no MVVM — `Camera2Fragment` is a plain Fragment
  with CameraDevice/CameraCaptureSession callbacks.
- Java only, support-library era patterns.
- KISS: legacy sample; don't add architecture layers.

## Landmines

- **Default branch is `master`, not `main`.**
- **JDK 8 + SDK 27/build-tools 27.0.3 required** — CI pins them.
- **README misdescribes the app** ("browsing and filtering cars") — the
  code is a camera/photo-filter/annotation app; trust the code.
- README says no wrapper — `gradlew` exists now (wrapper repair merged).
- `EditPictureActivity` has commented-out filter-selection code — filters
  are partially implemented.
- Room is `android.arch.persistence.room` 1.1.1 — old artifact group;
  don't mix with `androidx.room`.
- `Note` uses field `uUID` (odd casing) — match it.
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
