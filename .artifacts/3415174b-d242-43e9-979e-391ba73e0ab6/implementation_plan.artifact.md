# Fix: AGP Recommendation for compileSdk 37

The project is currently using `compileSdk 37`, but Android Gradle Plugin (AGP) 8.13.0 only supports up to SDK 36.1. This causes a recommendation warning and potential instability. To resolve this, I will upgrade AGP to a version that supports SDK 37.

## Proposed Changes

### [gradle](file:///Volumes/Data/Android/AFL-003-IPTV/gradle/libs.versions.toml)

#### [MODIFY] [libs.versions.toml](file:///Volumes/Data/Android/AFL-003-IPTV/gradle/libs.versions.toml)
- Upgrade `agp` version from `8.13.0` to `9.3.1`.

### [gradle-wrapper](file:///Volumes/Data/Android/AFL-003-IPTV/gradle/wrapper/gradle-wrapper.properties)

#### [MODIFY] [gradle-wrapper.properties](file:///Volumes/Data/Android/AFL-003-IPTV/gradle/wrapper/gradle-wrapper.properties)
- Upgrade Gradle distribution from `8.13` to `9.6.1` to ensure compatibility with AGP 9.3.1.

## Verification Plan

### Automated Verification
- Run `./gradlew :app:assembleDevDebug` to ensure the project builds with the new AGP version.
- Sync Gradle in the IDE.
