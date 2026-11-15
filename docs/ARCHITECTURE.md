# Architecture — CarFilters

Current-state map. Descriptive, not aspirational; see `docs/adr/` for why.

> **Note:** the repo name and README describe a car-filtering app; the
> actual code is a camera/photo app. This document describes the code.

## Shape

```
CarFilters/   (Android Gradle, AGP 8.7.3 / Gradle 8.9)
├── app/
│   ├── build.gradle     compileSdk/target 34, minSdk 21,
│   │                    applicationId com.hamzajg.carfilters,
│   │                    Room runtime+compiler 2.6.1 (androidx.room)
│   └── src/main/java/com/hamzajg/carfilters/
│       ├── MainActivity.java        gallery grid (photos + placeholders)
│       ├── CameraActivity.java      hosts the camera fragment
│       ├── Camera2Fragment.java     Camera2 capture pipeline (~1050 LOC)
│       ├── EditPictureActivity.java filter/rotate editing + photo note
│       │                            CRUD (markers tagged by UUID,
│       │                            tap→dialog view/edit/delete,
│       │                            idempotent delete+reinsert save,
│       │                            empty-note rejection)
│       ├── GalleryActivity.java     saved photos
│       ├── AutoFitTextureView.java  aspect-fit preview
│       ├── Note.java                Room entity (photo annotation)
│       ├── NoteDatabase.java        Room DB
│       └── DaoAccess.java           note CRUD per fileName
│                                    (insert/fetch/deleteByFileName/
│                                    update/delete)
└── .github/workflows/ci.yml   JDK 17 + SDK 34 → gradlew test
```

## Flows

- **Capture:** `MainActivity`/`CameraActivity` → `Camera2Fragment`
  (CameraDevice + CaptureSession + ImageReader → SurfaceTexture preview).
- **Edit:** captured photo → `EditPictureActivity` (filters, rotate;
  picker partially commented out).
- **Annotate:** `Note` rows (fileName, x, y, noteString) via Room —
  notes pinned to photos, queried per fileName. In `EditPictureActivity`:
  tap places a marker (UUID-tagged `ImageView`), tap on a marker opens a
  view/edit/delete dialog, edits/deletes write through `DaoAccess`
  immediately, and saving the screen is idempotent (delete-by-fileName +
  re-insert in one Room transaction). Empty note text is rejected.
- **Browse:** `GalleryActivity` + `MainActivity` grids.

## State

Room/SQLite via `NoteDatabase` + `DaoAccess` — only the Note entity
exists; photos themselves live in app storage (fileName references).

## Toolchain

JDK 17 + Android SDK 34 + build-tools 34.0.0 (AGP 8.7.3 / Gradle 8.9,
wrapper checked in). AndroidX (`androidx.*`, Room `androidx.room` 2.6.1).
