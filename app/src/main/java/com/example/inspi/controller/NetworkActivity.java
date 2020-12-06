package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;

import java.util.HashSet;
import java.util.Set;

public class NetworkActivity extends AppCompatActivity {

    private final BluetoothAdapter BLUETOOTHADAPTER = BluetoothAdapter.getDefaultAdapter();
    private final int REQUEST_ENABLE_BT = 0;

    private Set<BluetoothDevice> foundDevices = new HashSet<>();
    private TextView deviceTextView;
    private String deviceString;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    if (device.getName() != null) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                        foundDevices.add(device);
                    }
                }
            }
        }
    };

    public void openDiscoverDevices(View view) {
        boolean answer = BLUETOOTHADAPTER.startDiscovery();
        if (answer) {
            Toast.makeText(NetworkActivity.this, "Discover Devices", Toast.LENGTH_SHORT).show();
            for (BluetoothDevice bluetoothDevice: foundDevices) {
                deviceString = bluetoothDevice.getName() + bluetoothDevice.getAddress();
            }
            deviceTextView.setText(deviceString);
        } else {
            Toast.makeText(NetworkActivity.this, "Discover Device couldn't work.", Toast.LENGTH_SHORT).show();
        }
    }

    public void enableDiscoverability(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        // Test if the device supports Bluetooth
        if (BLUETOOTHADAPTER == null) {
            // Device doesn't support Bluetooth.
            Toast.makeText(NetworkActivity.this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        } else {
            if (!BLUETOOTHADAPTER.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        deviceTextView = findViewById(R.id.textView);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Bluetooth is working
            Toast.makeText(NetworkActivity.this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            // Bluetooth is not working
            Toast.makeText(NetworkActivity.this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
}