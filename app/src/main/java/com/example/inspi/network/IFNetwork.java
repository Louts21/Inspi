package com.example.inspi.network;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * This is the interface of all classes which quest it is to send data over a connection.
 */
public interface IFNetwork {
    /**
     * This method allows the user to discover devices.
     * @param bluetoothAdapter needs a BluetoothAdapter to start the discovery.
     */
    void discoverDevices(BluetoothAdapter bluetoothAdapter);

    /**
     * Allows other devices to find the current device.
     */
    void discoverability();

    /**
     * Starts the to discover other devices.
     * If shows if it fails or not.
     */
    void startDiscovery();

    /**
     * Unregister the receiver.
     * @param context is needed to know which receiver should be unregistered.
     */
    void stopReceiver(Context context);
}
