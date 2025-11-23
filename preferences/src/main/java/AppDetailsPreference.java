package de.firebirdberlin.preference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class AppDetailsPreference extends Preference {

    public AppDetailsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Context ctx = context;
        setOnPreferenceClickListener(
                preference -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", ctx.getPackageName(), null);
                    intent.setData(uri);
                    ctx.startActivity(intent);
                    return true;
                }
        );
    }
}
