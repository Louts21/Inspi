package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.network.IFNetwork;
import com.example.inspi.network.ImplNetwork;
import com.example.inspi.network.ServerAcceptThread;

import java.util.Set;

/**
 * This is the class which allows the user to view other devices and being discoverable.
 */
public class NetworkActivity extends AppCompatActivity {
    /**
     * This is a normal bluetoothAdapter to discover devices and to activate bluetooth within the application.
     */
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * Set of pairedDevices.
     */
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
     * The button will be needed to dis- and enable it.
     */
    private Button connectButton;

    /**
     * The name of the memo which will be send to another device.
     */
    private EditText memoName;

    /**
     * Object ifNetwork (Interface) of ImplNetwork (class).
     */
    private IFNetwork ifNetwork;

    /**
     * Thread that allows to accept bluetooth devices.
     */
    private ServerAcceptThread serverAcceptThread;

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
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        String[] files = this.fileList();
        for (BluetoothDevice device: pairedDevices) {
            if (connectEditText.getText().toString().equals(device.getAddress())) {
                counter1--;
                Toast.makeText(this, "Device address found", Toast.LENGTH_SHORT).show();
                for (String file: files) {
                    if (file.equals(memoName.getText().toString())) {
                        counter3--;
                        ifNetwork.transfer(device, file);
                    }
                }
            } else if (connectEditText.getText().toString().equals(device.getName())) {
                counter1--;
                Toast.makeText(this, "Device name found", Toast.LENGTH_SHORT).show();
                for (String file: files) {
                    if (file.contains(memoName.getText().toString())) {
                        counter3--;
                        ifNetwork.transfer(device, file);
                    }
                }
            } else {
                counter1++;
                counter2++;
                counter3++;
            }
        }
        if (counter1 == counter2) {
            Toast.makeText(this, "Device could not be found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Device could be found", Toast.LENGTH_SHORT).show();
        }
        if (counter3 == counter2) {
            Toast.makeText(this, "File could not be found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "File could be found", Toast.LENGTH_SHORT).show();
        }
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
                deviceTextView = findViewById(R.id.deviceTextView);
                pairedDevices = bluetoothAdapter.getBondedDevices();
                serverAcceptThread = new ServerAcceptThread(bluetoothAdapter, this);
                if (pairedDevices.size() > 0) {
                    deviceTextView.setText("Paired devices:" + '\n');
                    for (BluetoothDevice device: pairedDevices) {
                        deviceTextView.append(device.getAddress() + " - " + device.getName() + '\n');
                    }
                }
                serverAcceptThread.start();
            }
        }

        connectEditText = findViewById(R.id.macEditText);
        memoName = findViewById(R.id.titleEditTextNetworkActivity);
        connectButton = findViewById(R.id.connectButton);

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
        }
    }
}