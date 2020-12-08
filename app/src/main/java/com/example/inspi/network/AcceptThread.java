package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.inspi.R;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * This is the class which allows other devices to connect to this device.
 * This is the "Server".
 */
public class AcceptThread extends Thread implements IFNetwork {
    /**
     * This bluetoothServerSocket is needed to create a connection.
     */
    private final BluetoothServerSocket bluetoothServerSocket;

    /**
     * This method crates an UUID and decelerates the bluetoothServerSocket.
     * @param bluetoothAdapter is needed from networkActivity.
     */
    public AcceptThread(BluetoothAdapter bluetoothAdapter) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(String.valueOf(R.string.app_name), UUID.fromString("5cb2442e-88aa-4eab-a044-2955e0de3478"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        bluetoothServerSocket = tmp;
    }

    @Override
    public void run() {
        super.run();
        BluetoothSocket socket;
        while (true) {
            try {
                socket = bluetoothServerSocket.accept();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }

            if (socket != null) {
                manageMyConnectedSocket(socket);
                cancel();
                break;
            }
        }
    }

    @Override
    public void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {

    }

    /**
     * The sockets will be closed here.
     */
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
