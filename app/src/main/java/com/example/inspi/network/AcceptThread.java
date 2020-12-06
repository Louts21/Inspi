package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.inspi.R;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class AcceptThread extends Thread implements IFNetwork {
    private final BluetoothServerSocket bluetoothServerSocket;

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
        BluetoothSocket socket = null;
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

    public void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {

    }

    //Closes the socket
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
