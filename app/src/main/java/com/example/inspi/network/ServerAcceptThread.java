package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.inspi.model.File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ServerAcceptThread extends Thread {

    private final static String TAG = "INSPI_DEBUG_TAG_SAT";

    private final BluetoothServerSocket mmServerSocket;

    private final Context context;

    public ServerAcceptThread(BluetoothAdapter bluetoothAdapter, Context context) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Inspi", UUID.fromString("321eabe0-73a0-452b-8660-bdcbc24072ca"));
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
        this.context = context;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        try {
            socket = mmServerSocket.accept();
        } catch (IOException ioe) {
            Log.e(TAG, "Socket's accept() method failed", ioe);
        }
        if (socket != null) {
            // A connection was accepted. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(socket);
            Toast.makeText(context, "Received new data", Toast.LENGTH_SHORT).show();
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        DataInputStream dataInputStream;    //Filename and his input
        DataOutputStream dataOutputStream;  //Callback that he is done
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            String title = dataInputStream.readUTF();
            File file = new File(socket.getRemoteDevice().getAddress(), title);
            byte[] bytes1 = new byte[4096];
            int count1;
            while ((count1 = dataInputStream.read(bytes1)) > 0) {
                try (FileOutputStream fos = context.openFileOutput(file.getFileName(), Context.MODE_PRIVATE)) {
                    fos.write(bytes1, 0, count1);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            try {
                dataOutputStream.writeUTF("Done");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Cant get IStream as Server in manageMyConnectedSocket", ioe);
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not close the connect socket", ioe);
        }
    }
}
