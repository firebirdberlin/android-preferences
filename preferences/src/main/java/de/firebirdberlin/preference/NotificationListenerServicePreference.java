package de.firebirdberlin.preference;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class NotificationListenerServicePreference extends Preference {

    public NotificationListenerServicePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Context ctx = context;
        setOnPreferenceClickListener(
                preference -> {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    ctx.startActivity(intent);
                    return true;
                }
        );
    }
}
