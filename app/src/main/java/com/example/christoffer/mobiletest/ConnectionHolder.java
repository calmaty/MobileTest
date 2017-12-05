package com.example.christoffer.mobiletest;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 16-09-2017.
 */

public class ConnectionHolder {

    static ConnectionHolder ourInstance = new ConnectionHolder();

    public ArrayList<BluetoothConnectionService> Connections = new ArrayList<>();

    public static ConnectionHolder getInstance() {
        return ourInstance;
    }

}
