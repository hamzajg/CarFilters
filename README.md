# CarFilters

An Android app for browsing and filtering cars.

## What it does

`CarFilters` is a small Android application (`com.hamzajg.carfilters`) that lets a
user browse a car catalogue and narrow it down with filters. Data is stored
locally with Room.

## Tech stack

- **Language:** Java
- **Min SDK:** 21 · **Target/compile SDK:** 27
- **UI:** Android Support Library (`appcompat`, `design`, `constraint-layout`)
- **Persistence:** Room (`android.arch.persistence.room` 1.1.1)

## Project layout

```
app/
  src/main/java/com/hamzajg/carfilters/   # activities, adapters, Room entities/DAOs
  src/main/res/                           # layouts, drawables, strings
  build.gradle                            # app module config
build.gradle
settings.gradle
```

## Building

The project predates AndroidX and targets SDK 27, so it needs an Android SDK
with API 27 installed. It has no Gradle wrapper checked in; open the project in
Android Studio (or run Gradle 4.4+ with `ANDROID_HOME` set) and build the `app`
module:

```bash
gradle :app:assembleDebug
```

Install on a connected device/emulator:

```bash
gradle :app:installDebug
```

## Tests

- Unit tests: `app/src/test` (JUnit 4)
- Instrumented tests: `app/src/androidTest` (Espresso)

```bash
gradle :app:testDebugUnitTest
gradle :app:connectedDebugAndroidTest
```

## Status

Legacy sample app. The Android Support Library and SDK 27 are outdated; a
migration to AndroidX and a modern SDK would be required before any further
feature work.
