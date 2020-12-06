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

public class ConnectThread extends Thread implements IFNetwork {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter;

    // Will be stored like the pictures and memos
    // This map saves the name of the other devices and when he connected with him (last date)
    private Map<String, Date> rememberMap = new HashMap<>();

    // Will be saved into rememberMap and is the current date and time
    private Date date = new Date();

    private InputStream inputStream;
    private OutputStream outputStream;

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

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
