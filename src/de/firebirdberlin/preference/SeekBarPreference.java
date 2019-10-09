package de.firebirdberlin.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;


public class SeekBarPreference extends DialogPreference {
    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private static final String seekbar = "seekbar";

    private Context mContext;

    protected String mDialogMessage, mSuffix;
    protected int mDefault, mMax, mValue = 0;
    protected int mStep = 1;


    public SeekBarPreference(Context context, AttributeSet attrs) {

        super(context,attrs);
        mContext = context;
        // Get string value for dialogMessage
        int mDialogMessageId = attrs.getAttributeResourceValue(androidns, "dialogMessage", 0);

        if(mDialogMessageId == 0) {
            mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        }
        else {
            mDialogMessage = mContext.getString(mDialogMessageId);
        }

        // Get string value for suffix (text attribute in xml file) :
        int mSuffixId = attrs.getAttributeResourceValue(androidns, "text", 0);

        if(mSuffixId == 0) {
            mSuffix = attrs.getAttributeValue(androidns, "text");
        }
        else {
            mSuffix = mContext.getString(mSuffixId);
        }

        // Get default and max seekbar values :
        mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        mMax = attrs.getAttributeIntValue(androidns, "max", 100);
        mStep = attrs.getAttributeIntValue(seekbar, "step", 1);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        super.onSetInitialValue(defaultValue);
        int def = (defaultValue instanceof Integer) ? (Integer) defaultValue : 0;
        mValue = getPersistedInt(def);
    }

    protected void persist(int value) {
        mValue = value;
        persistInt(value);
        callChangeListener(value);
        notifyChanged();
    }
}
