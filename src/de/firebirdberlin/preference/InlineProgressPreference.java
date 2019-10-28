package de.firebirdberlin.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class InlineProgressPreference extends Preference {
    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    private static final String SEEKBAR = "http://schemas.android.com/apk/lib/android";

    private ProgressBar progressBar;

    public InlineProgressPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InlineProgressPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        super.onBindViewHolder(holder);
        View ret = holder.itemView;

        View summary = ret.findViewById(android.R.id.summary);
        if (summary != null) {
            ViewParent summaryParent = summary.getParent();
            if (summaryParent instanceof ViewGroup) {
                final LayoutInflater layoutInflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup summaryParent2 = (ViewGroup) summaryParent;
                layoutInflater.inflate(R.layout.inline_progressbar_preference, summaryParent2);

                progressBar = summaryParent2.findViewById(R.id.progressBar);
            }
        }
    }

    public void setProgress(int value){
        if (progressBar != null) {
            progressBar.setProgress(value);
            progressBar.invalidate();
        }
    }

    public void setMax(int value){
        if (progressBar != null) {
            progressBar.setMax(value);
            progressBar.invalidate();
        }
    }
}
