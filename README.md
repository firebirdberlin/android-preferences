# Android preferences library

## Installation
Build the library with
```
 $ ant release
```
In order to use it within your (ant) project add this line to the *project.properties*
```
android.library.reference.1=path/to/android-preferences
```

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

