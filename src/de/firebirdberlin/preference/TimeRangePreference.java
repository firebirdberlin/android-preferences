package de.firebirdberlin.preference;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TimePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;


public class TimeRangePreference extends Preference {
    private static final String TIMERANGE = "timerange";

    class SimpleTime {

        public int hour = 0;
        public int min = 0;

        public SimpleTime(long millis){
            if (millis < 0L) return;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
        }

        public SimpleTime(int minutes){
            this.hour = minutes / 60;
            this.min = minutes % 60;
        }

        public int toMinutes() {
            return this.hour * 60 + this.min;
        }

        public long getMillis() {
            return getCalendar().getTimeInMillis();
        }

        public Calendar getCalendar() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            Calendar now = Calendar.getInstance();
            if ( cal.before(now) ) {
                cal.add(Calendar.DATE, 1);
            }

            return cal;
        }
    }

    private Context mContext = null;
    private String key_suffix_end = "";
    private String key_suffix_start = "";
    private String label_end_text = "";
    private String label_start_text = "";
    private SimpleTime startTime;
    private SimpleTime endTime;
    private String keyStart;
    private String keyEnd;
    private String keyStartInMinutes;
    private String keyEndInMinutes;


    public TimeRangePreference(Context ctxt) {
        this(ctxt, null);
        mContext = getContext();
    }

    public TimeRangePreference(Context ctxt, AttributeSet attrs) {
        this(ctxt, attrs, android.R.attr.dialogPreferenceStyle);
        mContext = getContext();
        setValuesFromXml(attrs);
    }

    public TimeRangePreference(Context ctxt, AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);
        setValuesFromXml(attrs);
    }

    private void setValuesFromXml(AttributeSet attrs) {
        mContext = getContext();

        Resources res = mContext.getResources();
        String packageName = mContext.getPackageName();
        String res_label_start_text = getAttributeStringValue(attrs, TIMERANGE, "start_text", "start time");
        String res_label_end_text = getAttributeStringValue(attrs, TIMERANGE, "end_text", "end time");
        int identifierStart = res.getIdentifier(res_label_start_text, null, packageName);
        int identifierEnd = res.getIdentifier(res_label_end_text, null, packageName);
        if (identifierStart != 0 ) {
            label_start_text = res.getString(identifierStart);
        } else {
            label_start_text = "";
        }

        if (identifierEnd != 0 ) {
            label_end_text = res.getString(identifierEnd);
        } else {
            label_end_text = "";
        }

        key_suffix_start = getAttributeStringValue(attrs, TIMERANGE, "key_suffix_start", "_start");
        key_suffix_end = getAttributeStringValue(attrs, TIMERANGE, "key_suffix_end", "_end");

        keyStart = getKey() + key_suffix_start;
        keyEnd = getKey() + key_suffix_end;

        keyStartInMinutes = getKey() + key_suffix_start + "_minutes";
        keyEndInMinutes = getKey() + key_suffix_end + "_minutes";
    }

    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        setSummary(getSummary());
    }

    private static String getAttributeStringValue(AttributeSet attrs, String namespace,
                                                  String name, String defaultValue) {
        String value = attrs.getAttributeValue(namespace, name);
        if (value == null) value = defaultValue;

        return value;
    }

    @Override
    public void onClick() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                updateTime(startTime, selectedHour, selectedMinute);
                showDialogEndTime();

            }
        }, startTime.hour, startTime.min, DateFormat.is24HourFormat(mContext));
        setDialogTitle(mTimePicker, label_start_text);
        mTimePicker.show();
    }

    public void showDialogEndTime() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                updateTime(endTime, selectedHour, selectedMinute);
            }
        }, endTime.hour, endTime.min, DateFormat.is24HourFormat(mContext));
        setDialogTitle(mTimePicker, label_end_text);
        mTimePicker.show();
    }

    private void setDialogTitle(TimePickerDialog dialog, String title) {
        /* Due to a bug in the TimePicker view we set the title to an empty string
         * if the screen orientation is landscape
         * https://code.google.com/p/android/issues/detail?id=201766
         */
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (Build.VERSION.SDK_INT > 19
                && orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            dialog.setTitle("");
        } else {
            dialog.setTitle(title);
        }
    }

    private void updateTime(SimpleTime time, int hour, int min) {
        time.hour = hour;
        time.min = min;
        setSummary(getSummary());
        if (callChangeListener(time.getMillis())) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putInt(keyStartInMinutes, startTime.toMinutes());
            prefEditor.putInt(keyEndInMinutes, endTime.toMinutes());
            prefEditor.apply();
            notifyChanged();
        }
    }
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        int startInMinutes = sharedPreferences.getInt(keyStartInMinutes, -1);
        int endInMinutes = sharedPreferences.getInt(keyEndInMinutes, -1);

        if (startInMinutes > -1 && endInMinutes > -1) {
            startTime = new SimpleTime(startInMinutes);
            endTime = new SimpleTime(endInMinutes);
        } else {
            long startInMillis = sharedPreferences.getLong(keyStart, -1L);
            long endInMillis = sharedPreferences.getLong(keyEnd, -1L);

            startTime = new SimpleTime(startInMillis);
            endTime = new SimpleTime(endInMillis);
        }
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        if (startTime == null || endTime == null) {
            return null;
        }

        long start = startTime.getCalendar().getTimeInMillis();
        long end = endTime.getCalendar().getTimeInMillis();

        return DateFormat.getTimeFormat(mContext).format(new Date(start)) + " -- " +
               DateFormat.getTimeFormat(mContext).format(new Date(end));
    }

}
