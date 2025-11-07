package de.firebirdberlin.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;


public class SeekBarPreferenceDialogFragment extends PreferenceDialogFragmentCompat
                                             implements SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private TextView mValueText;

    SeekBarPreference preference;

    public static SeekBarPreferenceDialogFragment newInstance(SeekBarPreference preference) {
        final SeekBarPreferenceDialogFragment fragment = new SeekBarPreferenceDialogFragment(preference);

        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, preference.getKey());
        fragment.setArguments(bundle);
        return fragment;
    }

    private SeekBarPreferenceDialogFragment(SeekBarPreference preference) {
        this.preference = preference;
    }

    @Override
    protected void onPrepareDialogBuilder(@NonNull androidx.appcompat.app.AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preference.persist(mSeekBar.getProgress());
                    }
                }
        );
    }

    @Override
    protected View onCreateDialogView(@NonNull Context context) {
        //return super.onCreateDialogView(context);
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);
        TextView mSplashText = new TextView(context);
        if (preference.mDialogMessage != null) {
            mSplashText.setPadding(60, 20, 60, 20);
            mSplashText.setText(preference.mDialogMessage);
        }

        layout.addView(mSplashText);
        mValueText = new TextView(context);
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                               LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(context);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar,
                       new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.WRAP_CONTENT));

        mSeekBar.setMax(preference.mMax);
        mSeekBar.setProgress(preference.mValue);

        return layout;
    }

    @Override
    protected void onBindDialogView(@NonNull View v) {
        super.onBindDialogView(v);
        mSeekBar.setMax(preference.mMax);
        mSeekBar.setProgress(preference.mValue);
    }

    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        value = value / preference.mStep * preference.mStep;
        String t = String.valueOf(value);
        mValueText.setText(preference.mSuffix == null ? t : t.concat(" " + preference.mSuffix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {}
    @Override
    public void onStopTrackingTouch(SeekBar seek) {}


    @Override
    public void onDialogClosed(boolean positiveResult) {

    }
}
