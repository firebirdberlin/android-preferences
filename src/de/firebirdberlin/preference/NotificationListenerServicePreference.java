package de.firebirdberlin.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class NotificationListenerServicePreference extends Preference {

    public NotificationListenerServicePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Context ctx = context;
        setOnPreferenceClickListener(
                new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (Build.VERSION.SDK_INT >= 18) {
                            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            ctx.startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                }
        );
    }
}
