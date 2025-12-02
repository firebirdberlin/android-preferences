# Android preferences library
[![](https://jitpack.io/v/firebirdberlin/android-preferences.svg)](https://jitpack.io/#firebirdberlin/android-preferences)

## Sponsoring

If you find this library useful, you can support its development:

[![Sponsor on GitHub](https://img.shields.io/badge/Sponsor%20on%20GitHub-grey?logo=github)](https://github.com/sponsors/firebirdberlin)
[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-yellow?logo=buymeacoffee)](https://www.buymeacoffee.com/firebirdberlin)


## Installation
JitPack is a simple-to-use package repository for GitHub projects. To integrate `android-preferences` into your project, follow these steps:

1.  **Add the JitPack repository to your root `build.gradle`:**
    ```gradle
    allprojects {
        repositories {
            // ... other repositories
            maven { url 'https://jitpack.io' }
        }
    }
    ```

2.  **Add the dependency to your module's `build.gradle` (e.g., `app/build.gradle`):**
    ```gradle
    dependencies {
        implementation 'com.github.firebirdberlin:android-preferences:<latest-version>'
    }
    ```
    Replace `<latest-version>` with the [latest release version](https://github.com/firebirdberlin/android-preferences/releases).

## VersionPreference
### Usage
```
<de.firebirdberlin.preference.VersionPreference
    android:title="My Application Name"
    android:icon="@drawable/ic_clock"
    android:key="version"
    android:selectable="true">
    <intent
        android:action="android.intent.action.VIEW"
        android:data="https://play.google.com/store/apps/details?id=my.package.name"/>
</de.firebirdberlin.preference.VersionPreference>
```
