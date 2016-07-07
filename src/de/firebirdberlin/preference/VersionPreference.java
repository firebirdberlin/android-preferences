package de.firebirdberlin.preference;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

public class VersionPreference extends Preference {

    public VersionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            CharSequence title = getTitle();
            if ( title != null ) {
                setTitle(title.toString() + " v" + version);
            } else {
                setTitle(pInfo.packageName + " v" + version);
            }
        } catch (NameNotFoundException e){
            return;
        }
    }
}
