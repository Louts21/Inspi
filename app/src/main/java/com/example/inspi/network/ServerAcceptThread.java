package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.inspi.model.File;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Server site of my bluetooth service.
 * It will get data of a client and saves the data.
 * @author Kevin Jagielski
 */
public class ServerAcceptThread extends Thread {
    /**
     * Tag which will be shown if an error appears.
     */
    private final static String TAG = "INSPI_DEBUG_TAG_SAT";

    /**
     * The socket of server.
     */
    private final BluetoothServerSocket mmServerSocket;

    /**
     * The context of NetworkActivity.
     */
    private final Context context;

    /**
     * Constructor of ServerAcceptThread (class)
     * @param bluetoothAdapter is needed to create a socket.
     * @param context will be needed in another method.
     */
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

    @Override
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
        }
    }

    /**
     * This method will accept of file data.
     * @param socket is needed to get the I-Stream.
     */
    private void manageMyConnectedSocket(BluetoothSocket socket) {
        DataInputStream dataInputStream;    //Filename and his input
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String title = dataInputStream.readUTF();
            File file = new File(socket.getRemoteDevice().getAddress(), title);
            try (FileOutputStream fos = context.openFileOutput(file.getFileName(), Context.MODE_PRIVATE)) {
                fos.write(dataInputStream.readUTF().getBytes());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                Log.i(TAG, "Data received");
                toastAnywhere();
                cancel();
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Cant get IStream as Server in manageMyConnectedSocket", ioe);
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
                Toast.makeText(context, "Data received", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Closes the connect socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not close the connect socket", ioe);
        }
    }
}
