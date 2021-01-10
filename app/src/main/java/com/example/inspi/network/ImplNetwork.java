package com.example.inspi.network;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED;
import static android.bluetooth.BluetoothDevice.ACTION_FOUND;

/**
 * Implementation of IFNetwork (Interface).
 * This class manages everything about connection, bluetooth and data transfer.
 * @author Kevin Jagielski
 */
public class ImplNetwork implements IFNetwork {
    /**
     * This Log-Tag is specific created for this class to find issues.
     */
    private static final String TAG1 = "INSPI_DEBUG_TAG_BT";

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
                        deviceTextView.append(" " + '\n');
                        deviceTextView.append("Found devices:" + '\n');
                        for (BluetoothDevice bluetoothDevice : foundDevices) {
                            deviceTextView.append(bluetoothDevice.getAddress() + " - " + bluetoothDevice.getName() + '\n');
                        }
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
            Log.d(TAG1, "Successfully started Bluetooth discovery");
        } else {
            Log.d(TAG1, "Could not start Bluetooth discovery");
        }
    }

    @Override
    public void transfer(BluetoothDevice pairedDevice, String title) {
        ClientConnectThread clientConnectThread = new ClientConnectThread(pairedDevice, title);
        clientConnectThread.start();
    }

    @Override
    public void stopReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }

    /**
     * Inner class which will be used to send data to another device.
     */
    private class ClientConnectThread extends Thread{
        /**
         * Is a socket which will be needed to transfer the data.
         */
        private final BluetoothSocket bluetoothSocket;

        /**
         * Will be transferred to another device.
         */
        private final String memoTitle;

        /**
         * This tag will be shown if an error appears.
         */
        private final static String TAG2 = "INSPI_DEBUG_TAG_CCT";

        /**
         * Is the constructor of ClientConnectThread (class).
         * @param device is needed to create the socket.
         * @param title is needed to get the file which will be send.
         */
        public ClientConnectThread(BluetoothDevice device, String title) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            memoTitle = title;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("321eabe0-73a0-452b-8660-bdcbc24072ca"));
            } catch (IOException e) {
                Log.e(TAG2, "Socket's create() method failed", e);
            }
            bluetoothSocket = tmp;
        }

        @Override
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    Log.i(TAG2, "Could not close the client socket", closeException);
                }
                return;
            }
            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(bluetoothSocket);
            cancel();
        }

        /**
         * This method transfers the file/data to another device.
         * @param bluetoothSocket is needed to get the O-Stream.
         */
        private void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {
            DataOutputStream dataOutputStream;  //File and his title
            try {
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(bluetoothSocket.getOutputStream()));
                FileInputStream fileInputStream = context.openFileInput(memoTitle);
                dataOutputStream.writeUTF(memoTitle);
                dataOutputStream.flush();
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    Log.i(TAG2, "Data send");
                    toastAnywhere();
                    dataOutputStream.writeUTF(stringBuilder.toString());
                    dataOutputStream.flush();
                }
            } catch (Exception e) {
                Log.e(TAG2, "Cant get OStream as Client in manageMyConnectedSocket", e);
            }
        }

        /**
         * Creates a toast which can be shown in the main thread.
         */
        public void toastAnywhere() {
            Handler toastHandler = new Handler(Looper.getMainLooper());
            toastHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Data send", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * Closes the client socket and causes the thread to finish.
         */
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG2, "Could not close the client socket", e);
            }
        }
    }
}