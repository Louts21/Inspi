package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * This is the interface of all classes which quest it is to send data over a connection.
 */
public interface IFNetwork {

    void discoverDevices(BluetoothAdapter bluetoothAdapter);

    void discoverability();

    void stopReceiver(Context context);
}
