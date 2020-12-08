package com.example.inspi.network;

import android.bluetooth.BluetoothSocket;

/**
 * This is the interface of all classes which quest it is to send data over a connection.
 */
public interface IFNetwork {
    /**
     * Here starts the transaction of both devices.
     * @param bluetoothSocket is needed to transfer data.
     */
    void manageMyConnectedSocket(BluetoothSocket bluetoothSocket);
}
