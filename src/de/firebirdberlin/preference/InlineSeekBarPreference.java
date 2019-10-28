package de.firebirdberlin.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class InlineSeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    private static final String SEEKBAR = "http://schemas.android.com/apk/lib/android";
    private static final int DEFAULT_VALUE = 50;

    private int maxValue = 100;
    private int minValue = 0;
    private int interval = 1;
    private int currentValue;
    private String unitsLeft = "";
    private String unitsRight = "";

    private AppCompatSeekBar seekBar;
    private TextView statusText;
    AttributeSet attrs;
    int defStyle;

    public InlineSeekBarPreference(Context context) {
        super(context);
    }

    public InlineSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setValuesFromXml(attrs);
        this.attrs = attrs;
    }

    public InlineSeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setValuesFromXml(attrs);
        this.attrs = attrs;
        this.defStyle = defStyle;
    }

    private void setValuesFromXml(AttributeSet attrs) {
        maxValue = attrs.getAttributeIntValue(SEEKBAR, "max", 100);
        minValue = attrs.getAttributeIntValue(SEEKBAR, "min", 0);

        unitsLeft = getAttributeStringValue(attrs, SEEKBAR, "unitsLeft", "");
        String units = getAttributeStringValue(attrs, SEEKBAR, "units", "");
        unitsRight = getAttributeStringValue(attrs, SEEKBAR, "unitsRight", units);

        try {
            String intervalStr = attrs.getAttributeValue(SEEKBAR, "interval");
            if (intervalStr != null) interval = Integer.parseInt(intervalStr);
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View ret = holder.itemView;

        View summary = ret.findViewById(android.R.id.summary);
        if (summary != null) {
            ViewParent summaryParent = summary.getParent();
            if (summaryParent instanceof ViewGroup) {
                final LayoutInflater layoutInflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup summaryParent2 = (ViewGroup) summaryParent;

                View view = summaryParent2.findViewWithTag("custom");
                if (view == null) {
                    layoutInflater.inflate(R.layout.inline_seekbar_preference, summaryParent2);
                }

                seekBar = summaryParent2.findViewById(R.id.seekBar);
                seekBar.setMax(maxValue - minValue);
                seekBar.setProgress(currentValue - minValue);
                seekBar.setOnSeekBarChangeListener(this);
                seekBar.setEnabled(isEnabled());

                statusText = summaryParent2.findViewById(R.id.seekBarPrefValue);
                statusText.setText(String.valueOf(currentValue));
                statusText.setMinimumWidth(30);
                statusText.setEnabled(isEnabled());

                TextView unitsLeftView = summaryParent2.findViewById(R.id.seekBarPrefUnitsLeft);
                unitsLeftView.setText(unitsLeft);
                unitsLeftView.setEnabled(isEnabled());

                TextView unitsRightView = summaryParent2.findViewById(R.id.seekBarPrefUnitsRight);
                unitsRightView.setText(unitsRight);
                unitsRightView.setEnabled(isEnabled());
            }
        }
    }

    public void setProgress(int progress) {
        if (seekBar == null) {
            return;
        }
        if (progress >= minValue && progress <= maxValue) {
            seekBar.setProgress(progress - minValue);
            notifyChanged();
        }
    }

    //region OnSeekBarChangeListener interface
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = progress + minValue;

        if (newValue > maxValue) {
            newValue = maxValue;
        } else if (newValue < minValue) {
            newValue = minValue;
        } else if (interval != 1 && newValue % interval != 0) {
            newValue = Math.round(((float) newValue) / interval) * interval;
        }

        // change rejected, revert to the previous value
        if (!callChangeListener(newValue)) {
            seekBar.setProgress(currentValue - minValue);
            return;
        }

        // change accepted, store it
        currentValue = newValue;
        if (statusText != null) statusText.setText(String.valueOf(newValue));
        persistInt(newValue);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    //endregion

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {
        return ta.getInt(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        int def = minValue;
        if (defaultValue instanceof Integer) {
            def = (int) defaultValue;
        }
        currentValue = getPersistedInt(def);
    }

    private static String getAttributeStringValue(AttributeSet attrs, String namespace,
                                                  String name, String defaultValue) {
        String value = attrs.getAttributeValue(namespace, name);
        if (value == null) value = defaultValue;

        return value;
    }

    public void setSecondaryProgress(int value){
        if (seekBar != null) {
            seekBar.setSecondaryProgress(value);
            seekBar.invalidate();
        }
    }

    public void setRange(int min, int max) {
        this.minValue = min;
        this.maxValue = max;
        if (seekBar != null) {
            seekBar.setMax(max - min);
            seekBar.invalidate();
        }
    }

    public void setMax(int value){
        if (seekBar != null) {
            seekBar.setMax(value);
            seekBar.invalidate();
        }
    }

    public int getProgress() {
        if (seekBar != null) return seekBar.getProgress();
        return 0;
    }

}
