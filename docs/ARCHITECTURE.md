# Architecture — CarFilters

Current-state map. Descriptive, not aspirational; see `docs/adr/` for why.

> **Note:** the repo name and README describe a car-filtering app; the
> actual code is a camera/photo app. This document describes the code.

## Shape

```
CarFilters/   (Android Gradle, AGP 3.1.4 / Gradle 4.4)
├── app/
│   ├── build.gradle     compileSdk/target 27, minSdk 21,
│   │                    applicationId com.hamzajg.carfilters,
│   │                    Room runtime+compiler 1.1.1 (android.arch)
│   └── src/main/java/com/hamzajg/carfilters/
│       ├── MainActivity.java        gallery grid (photos + placeholders)
│       ├── CameraActivity.java      hosts the camera fragment
│       ├── Camera2Fragment.java     Camera2 capture pipeline (~1050 LOC)
│       ├── EditPictureActivity.java filter/rotate editing
│       ├── GalleryActivity.java     saved photos
│       ├── AutoFitTextureView.java  aspect-fit preview
│       ├── Note.java                Room entity (photo annotation)
│       ├── NoteDatabase.java        Room DB
│       └── DaoAccess.java           note CRUD per fileName
└── .github/workflows/ci.yml   JDK 8 + SDK 27/27.0.3 → gradlew test
```

## Flows

- **Capture:** `MainActivity`/`CameraActivity` → `Camera2Fragment`
  (CameraDevice + CaptureSession + ImageReader → SurfaceTexture preview).
- **Edit:** captured photo → `EditPictureActivity` (filters, rotate;
  picker partially commented out).
- **Annotate:** `Note` rows (fileName, x, y, noteString) via Room —
  notes pinned to photos, queried per fileName.
- **Browse:** `GalleryActivity` + `MainActivity` grids.

## State

Room/SQLite via `NoteDatabase` + `DaoAccess` — only the Note entity
exists; photos themselves live in app storage (fileName references).

## Toolchain

JDK 8 + Android SDK 27 + build-tools 27.0.3 (AGP 3.1.4/Gradle 4.4).
Pre-AndroidX (`android.support.*`, `android.arch.*`).
