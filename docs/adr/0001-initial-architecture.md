# ADR 0001 — Initial architecture

Date: 2026-11-13 (baseline recorded retroactively)
Status: accepted (describes what is built)

## Context

An Android camera/photo app (package `carfilters`) with capture, editing,
gallery, and photo annotations — kept as a learning/sample project.

## Decisions

1. **Camera2 API directly.** `Camera2Fragment` uses CameraDevice /
   CaptureSession / ImageReader — no camera library abstraction. Rationale:
   learning the platform API; ~1050 lines is the cost of that choice.
2. **Room for annotations only.** `Note` entity (UUID, fileName, x, y,
   noteString) + `DaoAccess` — photos aren't in the DB, only pointers
   (fileName) + pin coordinates. Rationale: minimal persistence for the
   note-on-photo feature.
3. **Activity/Fragment structure, no MVVM.** Plain AppCompat activities +
   one Camera2 fragment; UI logic in activities. Rationale: era-typical
   sample style.
4. **Pre-AndroidX stack, frozen.** support library + `android.arch` Room
   1.1.1, SDK 27, JDK 8, AGP 3.1.4/Gradle 4.4. Rationale: period sample;
   modernisation deferred (same as sibling Android repos).
5. **Filter editing partially built.** `EditPictureActivity` has
   rotate + commented-out filter selection — filters are a stub.
   Rationale: work-in-progress captured honestly.

## Consequences / open questions

- **README is wrong about the app's purpose** ("cars") — corrected in
  AGENTS.md/ARCHITECTURE.md; the package name is legacy.
- No tests beyond template `ExampleUnitTest`/`ExampleInstrumentedTest`.
- Camera2 on modern devices needs runtime permissions + CameraX would be
  the modern path — a migration decision for later.
- Room 1.1.1 (`android.arch`) can't mix with `androidx.room` — any DB work
  stays on the old artifact until an AndroidX migration.
