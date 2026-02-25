# Mindgate App

## Requirements

- **Android Studio** (recommended) or IntelliJ IDEA
- **JDK 17**
- **Android SDK** (Android Studio will install what’s needed)
- (Optional) Physical Android device or Emulator

---

## Setup (Secrets)

In `local.properties` add these secrets:

```agsl
WEB_CLIENT_ID= <firebase-web-client-id>
ZEGOCLOUD_APP_ID = <Application-ID>
ZEGOCLOUD_APP_SIGN = <ZegoCloud-App-Sign>
```

> `local.properties` is typically not committed. Don’t paste real secrets into GitHub.

---

## Run (Android)

### 1) Clone
```bash
git clone https://github.com/mindgatenation/mindgate_app.git
cd mindgate_app
```

### 2) Open in Android Studio
Android Studio → **Open** → select the `mindgate_app` folder → wait for Gradle sync.

### 3) Run
- Select an emulator/device
- Click **Run** in Android Studio

Or via terminal:

```bash
./gradlew assembleDebug
# optional install (requires connected device/emulator)
./gradlew installDebug
```

### Build release (if signing is configured)
```bash
./gradlew assembleRelease
```

---

## Project Structure

### Top-level
```text
mindgate_app/
├─ app/                    # Main Android app module
├─ gradle/                 # Gradle wrapper support files
├─ .github/                # GitHub workflows/config
├─ build.gradle.kts        # Root Gradle build (Kotlin DSL)
├─ settings.gradle.kts     # Gradle settings (modules, repos)
├─ gradle.properties       # Gradle/Android build properties
├─ gradlew                # Gradle wrapper (macOS/Linux)
├─ gradlew.bat            # Gradle wrapper (Windows)
└─ README.md
```

### App code layout (key package)
The primary app package is:

`app/src/main/java/com/mindgate/mindgateapp/`

Contents:

```text
app/src/main/java/com/mindgate/mindgateapp/
├─ MainActivity.kt
├─ MindegateApp.kt
├─ Values.kt
├─ data/         # data layer (models, repositories, network/db, etc.)
├─ di/           # dependency injection setup
├─ ui/           # UI layer (screens/components)
└─ viewmodels/   # ViewModels (presentation logic/state)
```

---

## Useful Gradle commands

```bash
./gradlew tasks                 # list tasks
./gradlew clean                 # clean build outputs
./gradlew assembleDebug         # build debug APK
./gradlew test                  # run unit tests (if present)
./gradlew connectedAndroidTest  # instrumentation tests (if configured)
```