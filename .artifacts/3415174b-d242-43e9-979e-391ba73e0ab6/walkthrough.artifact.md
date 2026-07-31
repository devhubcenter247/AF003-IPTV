# Walkthrough: Fixed Compose Preview Render Issue

I have resolved the `ClassNotFoundException: androidx.compose.ui.tooling.ComposeViewAdapter` error that was preventing the Compose Previews from rendering in Android Studio.

## Changes Made

### Gradle Configuration

#### [libs.versions.toml](file:///Volumes/Data/Android/AFL-003-IPTV/gradle/libs.versions.toml)
- Defined the `androidx-ui-tooling` library.

#### [app/build.gradle.kts](file:///Volumes/Data/Android/AFL-003-IPTV/app/build.gradle.kts)
- Added `debugImplementation(libs.androidx.ui.tooling)` to ensure the tooling adapter is available for Previews without bloating the release APK.

## Verification Results

### Automated Tests
- Executed `./gradlew :app:assembleDevDebug` which finished successfully.

> [!NOTE]
> The `product` flavor build has an existing issue with `google-services.json` which is unrelated to these changes.
