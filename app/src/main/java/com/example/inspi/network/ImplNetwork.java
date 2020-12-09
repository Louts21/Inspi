package com.example.inspi.network;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of IFNetwork (Interface).
 * This class manages everything about connection, bluetooth and data transfer.
 */
public class ImplNetwork implements IFNetwork {
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
     * This BroadcastReceiver is needed to find other devices.
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("ShowToast")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    Toast.makeText(context, "Device found " + device.getName(), Toast.LENGTH_SHORT);
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
                    }
                }
            }
        }
    };

    public ImplNetwork(Context context, TextView textView, EditText editText) {
        this.context = context;
        this.deviceTextView = textView;
        this.connectEditText = editText;
    }

    public void discoverDevices(BluetoothAdapter bluetoothAdapter) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    public void discoverability() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    public void stopReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }

    /**
     * Innerclass of ImplNetwork.
     * Tries to connect to another device.
     */
    private static class ConnectThread extends Thread {
        /**
         * This bluetoothSocket is needed to connect to another device.
         */
        private final BluetoothSocket bluetoothSocket;

        /**
         * Here is the information about the other device.
         */
        private final BluetoothDevice bluetoothDevice;

        /**
         * This object is from NetworkActivity and is needed to continue the connection.
         */
        private final BluetoothAdapter bluetoothAdapter;

        /**
         * Saves when and how the device connected in a while.
         */
        private Map<String, String> rememberMap = new HashMap<>();

        /**
         * Is needed to transfer data to another device
         */
        private final OutputStream outputStream;

        /**
         * Context of our application.
         */
        private final Context context;

        /**
         * Constructor of ConnectThread.
         * @param device is the device which we want to connect.
         * @param bluetoothAdapter is needed to transfer data.
         */
        public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Context context) {
            BluetoothSocket tmp = null;
            OutputStream outputStream = null;
            this.bluetoothDevice = device;
            this.bluetoothAdapter = bluetoothAdapter;
            this.rememberMap.put(device.getAddress(), null);

            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("5cb2442e-88aa-4eab-a044-2955e0de3478"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            bluetoothSocket = tmp;

            try {
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            this.outputStream = outputStream;
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
            } catch (IOException ioe) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }
            manageMyConnectedSocket(bluetoothSocket);
            cancel();
        }

        @SuppressLint("ShowToast")
        public void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {
            //Todo
            String[] files = context.fileList();
            try {
                for (String file: files) {
                    outputStream.write(file.getBytes());
                }
            } catch (IOException ioe) {
                Toast.makeText(context, "Couldn't send data", Toast.LENGTH_SHORT);
            }
            //rememberMap.put(bluetoothDevice.getAddress(), file.currentTimeGetter());
            Toast.makeText(context, "Data send", Toast.LENGTH_SHORT);
        }

        /**
         * Cancels all sockets in this class.
         */
        public void cancel() {
            try {
                bluetoothSocket.close();
                outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
