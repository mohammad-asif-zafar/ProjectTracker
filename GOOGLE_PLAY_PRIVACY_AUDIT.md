# Google Play Privacy Compliance & Technical Audit Report (`GOOGLE_PLAY_PRIVACY_AUDIT.md`)

**Application Name:** ProTrack – Project and Task Tracker  
**Developer:** Mohammad Asif Zafar  
**Audit Date:** November 2026  

---

## 1. Permissions Audit (`AndroidManifest.xml`)
- **Declared Permissions:** None.
- **Internet Permission (`android.permission.INTERNET`)**: Not declared. ProTrack operates entirely offline.
- **Runtime Permissions:** Zero runtime permissions requested.
- **Exported Components:** Only `MainActivity` is declared with `.MainActivity` and standard main launcher intent filter. No exported broadcast receivers, content providers, or background services.

## 2. Dependency and SDK Audit (`libs.versions.toml` & `build.gradle.kts`)
- **Analytics/Advertising/Crash Reporting SDKs:** None.
- **Core Libraries:** Kotlin Multiplatform, Compose Multiplatform, Koin DI, KotlinX Serialization, and SQLDelight (`app.cash.sqldelight`).

## 3. Network and Data Flow Audit
- **Network Clients:** Zero HTTP client libraries (no Ktor, no Retrofit, no OkHttp).
- **Outbound Traffic:** Zero outbound network requests or remote API calls in normal operation.

## 4. Local Data and Backup Audit
- **Persistence:** SQLDelight SQLite database (`ProjectDatabase`) storing projects, subtasks, and activity logs locally.
- **Android Backup (`android:allowBackup="true"`)**: Enabled in manifest. As part of standard Android operating system behavior, local app databases and preferences may be included in encrypted device backups (such as Google Drive backup) or transferred during device-to-device setup, managed entirely by the Android system framework without third-party or developer server access.

## 5. Data Deletion and Retention
- **Project & Subtask Deletion:** Fully implemented with `ON DELETE CASCADE` foreign key constraints in SQLite schema (`SubtaskEntity.sq`). Deleting a project automatically deletes all associated subtasks and related activity logs.
- **App Uninstall / Clear Storage:** Uninstalling the app or clearing storage removes local database files. Cloud backups (if device backup is enabled) persist until overwritten or deleted via device/cloud backup settings.

## 6. Google Play Data Safety Answers
- **Data Collected/Shared:** None by developer (local app database only).
- **Data Backed Up:** Local project/task data may be included in Android system backups if enabled by the user.
- **Account Requirement:** No account required.
- **Privacy Policy URL:** `https://mohammad-asif-zafar.github.io/ProjectTracker/privacy-policy.html`.

## 7. Items Requiring Developer Confirmation
- **Effective Date:** Listed as *"To be confirmed"* in the privacy policy; update prior to store submission.
- **Live Hosting:** Verify GitHub Pages deployment once published.
