# Android preferences library

## Installation
Build the library with
```
 $ ant release
```
Copy bin/classes.jar to *path/to/your/project/lib/android-preferences.jar*

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

