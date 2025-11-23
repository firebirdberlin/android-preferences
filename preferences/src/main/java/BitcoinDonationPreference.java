package de.firebirdberlin.preference;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class BitcoinDonationPreference extends Preference {
    private static final String attr_namespace = "bitcoindonation";
    private String bitcoinAddress = "1Ek8CxQeM7aoTNVunfL2jNjkiLa6cZqF3b";
    private Context context;

    public BitcoinDonationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        bitcoinAddress =
                getAttributeStringValue(
                        attrs, attr_namespace, "bitcoinAddress", bitcoinAddress
                );
    }

    @Override
    public void onClick() {
        try {
            Intent bitcoinIntent = new Intent(Intent.ACTION_VIEW,
                                              Uri.parse("bitcoin:" + bitcoinAddress));
            context.startActivity(bitcoinIntent);
        }
        catch ( ActivityNotFoundException e) {
            // cannot handle the bitcoin request.
            openPlayStore();
        }

    }

    private void openPlayStore() {
        try {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.mycelium.wallet"));
            context.startActivity(marketIntent);
        }
        catch ( ActivityNotFoundException e) {
            // There's even no market installed. Do nothing.
        }
    }

    private static String getAttributeStringValue(
            AttributeSet attrs, String namespace, String name, String defaultValue
    ) {
        String value = attrs.getAttributeValue(namespace, name);
        if (value == null) value = defaultValue;

        return value;
    }
}
