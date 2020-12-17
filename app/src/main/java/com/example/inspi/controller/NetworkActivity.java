package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.network.IFNetwork;
import com.example.inspi.network.ImplNetwork;

import java.util.HashSet;
import java.util.Set;

/**
 * This is the class which allows the user to view other devices and being discoverable.
 */
public class NetworkActivity extends AppCompatActivity {
    /**
     * This is a normal bluetoothAdapter to discover devices and to activate bluetooth within the application.
     */
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private Set<BluetoothDevice> pairedDevices;

    /**
     * I have no idea what this is for.
     */
    private final int REQUEST_ENABLE_BT = 0;

    /**
     * EditText to use the input of the user.
     */
    private EditText connectEditText;

    /**
     * We need this textView-object to manipulate
     * the one on the view object of activity_network.
     */
    private TextView deviceTextView;

    /**
     * Needed to make the discoverDevices button
     * unusable.
     */
    private Button discoverButton;

    /**
     * Object ifNetwork (Interface) of ImplNetwork (class).
     */
    private IFNetwork ifNetwork;

    /**
     * This method allows the user to discover devices.
     * @param view needs a view-object to use onClick().
     */
    public void openDiscoverDevices(View view) {
        ifNetwork.discoverDevices(bluetoothAdapter);
    }

    /**
     * Allows other devices to find the current device.
     * @param view needed to use onClick().
     */
    public void enableDiscoverability(View view) {
        ifNetwork.discoverability();
    }

    /**
     * This method starts the transfer of data.
     * @param view is needed for toClick().
     */
    public void openConnect(View view) {
        //Todo
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        // Test if the device supports Bluetooth
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth.
            Toast.makeText(NetworkActivity.this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                pairedDevices = bluetoothAdapter.getBondedDevices();
            }
        }
        connectEditText = findViewById(R.id.macEditText);
        deviceTextView = findViewById(R.id.deviceTextView);
        discoverButton = findViewById(R.id.scanButton);
        for (BluetoothDevice device: pairedDevices) {
            deviceTextView.setText(device.getAddress() + '\n');
        }
        // As long as it wont work i will disable it.
        discoverButton.setEnabled(false);
        ifNetwork = new ImplNetwork(this, deviceTextView, connectEditText, bluetoothAdapter);
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
        } else if (requestCode == 300) {
            Toast.makeText(NetworkActivity.this, "You can be discovered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ifNetwork.stopReceiver(this);
    }
}