package de.firebirdberlin.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class AndroidPreferencesPreference extends Preference {

    private static final String prefs = "prefs";
    public AndroidPreferencesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        String which = attrs.getAttributeValue(prefs, "which");
        final Intent intent;
        switch (which) {
            case "NotificationListener":
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                break;
            case "Sound":
                intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                break;
            case "TTS":
                intent = new Intent("com.android.settings.TTS_SETTINGS");
                break;
            default:
                throw new IllegalArgumentException("not implemented");


        }
        final Context ctx = context;
        setOnPreferenceClickListener(
                preference -> {
                    ctx.startActivity(intent);
                    return true;
                }
        );
    }
}
