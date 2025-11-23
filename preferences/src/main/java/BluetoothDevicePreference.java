package de.firebirdberlin.preference;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;

import androidx.preference.MultiSelectListPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BluetoothDevicePreference extends MultiSelectListPreference {

    private static final String NAMESPACE = "pref";

    private String textBluetoothOn;
    private String textBluetoothOff;
    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        handleBluetoothState(false);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        handleBluetoothState(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    public BluetoothDevicePreference(Context context) {
        super(context);
        initReceiver(context);
    }

    public BluetoothDevicePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initReceiver(context);
        setValuesFromXml(attrs);
    }

    public BluetoothDevicePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initReceiver(context);
        setValuesFromXml(attrs);
    }

    public BluetoothDevicePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initReceiver(context);
        setValuesFromXml(attrs);
    }

    static Set<BluetoothDevice> getBondedBluetoothDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return new HashSet<>();
        }
        return bluetoothAdapter.getBondedDevices();

    }

    private void initReceiver(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            setEnabled(false);
            return;
        }
        handleBluetoothState(mBluetoothAdapter.isEnabled());

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(bluetoothStateReceiver, filter);
    }

    private void init() {
        Set<BluetoothDevice> devices = getBondedBluetoothDevices();

        ArrayList<String> entries = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (BluetoothDevice device : devices) {
            values.add(device.getAddress());
            entries.add(device.getName());
        }
        setEntries(entries.toArray(new String[0]));
        setEntryValues(values.toArray(new String[0]));
        Set<String> defaults = new HashSet<>();
        setDefaultValue(defaults);
    }

    @Override
    protected void onClick() {
        super.onClick();
        init();
    }

    void handleBluetoothState(boolean on) {
        if (on) {
            setSummary(textBluetoothOn);
            setEnabled(true);
        } else {
            setSummary(textBluetoothOff);
            setEnabled(false);
        }
    }

    private void setValuesFromXml(AttributeSet attrs) {
        textBluetoothOff = getAttributeStringValue(
                attrs, NAMESPACE, "text_bluetooth_off", "bluetooth OFF"
        );
        textBluetoothOn = getAttributeStringValue(
                attrs, NAMESPACE, "text_bluetooth_on", ""
        );

    }

    private String getAttributeStringValue(
            AttributeSet attrs, String namespace, String name, String defaultValue
    ) {

        String value = null;
        int resId = attrs.getAttributeResourceValue(namespace, name, 0);

        if (resId == 0) {
            value = attrs.getAttributeValue(namespace, name);
            if (value == null)
                value = defaultValue;
        } else {
            value = getContext().getResources().getString(resId);
        }

        return value;
    }
}