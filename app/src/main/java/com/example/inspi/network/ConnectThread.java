package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class creates a connection with another device.
 * This is the "Client".
 */
public class ConnectThread extends Thread implements IFNetwork {
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
    private Map<String, Date> rememberMap = new HashMap<>();

    /**
     * Is needed to tell the current date.
     */
    private Date date = new Date();

    /**
     * Do I need it?
     */
    private InputStream inputStream;

    /**
     * Is needed to transfer data to another device
     */
    private OutputStream outputStream;

    /**
     *
     * @param device
     * @param bluetoothAdapter
     */
    public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        BluetoothSocket tmp = null;
        this.bluetoothDevice = device;
        this.bluetoothAdapter = bluetoothAdapter;
        this.rememberMap.put(device.getAddress(), null);

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("5cb2442e-88aa-4eab-a044-2955e0de3478"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        bluetoothSocket = tmp;
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
    }

    @Override
    public void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = bluetoothSocket.getInputStream();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Cancels all sockets in this class.
     */
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
