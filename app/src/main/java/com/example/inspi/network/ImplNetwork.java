package com.example.inspi.network;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.controller.CreateMemoActivity;
import com.example.inspi.controller.NetworkActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED;
import static android.bluetooth.BluetoothDevice.ACTION_FOUND;

/**
 * Implementation of IFNetwork (Interface).
 * This class manages everything about connection, bluetooth and data transfer.
 */
public class ImplNetwork implements IFNetwork {
    /**
     * This Log-Tag is specific created for this class to find issues.
     */
    private static final String TAG = "INSPI_DEBUG_TAG_BT";

    /**
     * All discovered devices will be saved here.
     */
    private final Set<BluetoothDevice> foundDevices = new HashSet<>();

    /**
     * We need this textView-object to manipulate
     * the one on the view object of activity_network.
     */
    private final TextView deviceTextView;

    /**
     * It saves the name of a discovered device.
     */
    private String deviceString;

    /**
     * Context of application.
     */
    private final Context context;

    /**
     * EditText to use the input of the user.
     */
    private final EditText connectEditText;

    /**
     * BluetoothAdapter of NetworkActivity.
     * Is needed to start the discover of other devices.
     */
    private final BluetoothAdapter bluetoothAdapter;

    /**
     * This BroadcastReceiver is needed to find other devices.
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("ShowToast")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    Toast.makeText(context, "Device found " + device.getName(), Toast.LENGTH_SHORT).show();
                    foundDevices.add(device);
                }
            } else {
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Toast.makeText(context, "Discover Devices", Toast.LENGTH_SHORT).show();
                } else {
                    if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        for (BluetoothDevice bluetoothDevice: foundDevices) {
                            deviceString = bluetoothDevice.getAddress() + '\n';
                        }
                        deviceTextView.setText(deviceString);
                        Toast.makeText(context, "Discover Devices finished", Toast.LENGTH_SHORT).show();
                        stopReceiver(context);
                    }
                }
            }
        }
    };

    /**
     * Constructor of ImplNetwork.
     * @param context context of NetworkActivity.
     * @param textView this textView shows the discovered devices.
     * @param editText this editText allows the user to write down which device he will connect.
     * @param bluetoothAdapter is needed to start the discover for devices.
     */
    public ImplNetwork(Context context, TextView textView, EditText editText, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.deviceTextView = textView;
        this.connectEditText = editText;
        this.bluetoothAdapter = bluetoothAdapter;
    }


    @Override
    public void discoverDevices(BluetoothAdapter bluetoothAdapter) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(ACTION_FOUND);
        filter.addAction(ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(receiver, filter);
        startDiscovery();
    }

    @Override
    public void discoverability() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    @Override
    public void startDiscovery() {
        if (bluetoothAdapter.startDiscovery()) {
            Log.d(TAG, "Successfully started Bluetooth discovery");
        } else {
            Log.d(TAG, "Could not start Bluetooth discovery");
        }
    }

    public void transfer() {

    }

    @Override
    public void stopReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }
}