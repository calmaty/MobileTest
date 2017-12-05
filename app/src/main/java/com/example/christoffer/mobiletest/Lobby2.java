package com.example.christoffer.mobiletest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class Lobby2 extends AppCompatActivity {

    private static final String TAG = "Lobby2";

    ConnectionHolder CH;

    boolean reg2;

    public ArrayList<BluetoothDevice> ConnectedDevices;

    public ArrayList<BluetoothDevice> mBTDevices;

    BluetoothAdapter mBluetoothAdapter;

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*if(done == false )
            {*/
            Object qco = intent.getSerializableExtra("dto");

            DTO dto = (DTO) qco;

            ScoreBoard.getInstance().WinningPoint = dto.winningPoint;
            if (reg2) {
                unregisterReceiver(mBroadcastReceiver2);
                reg2 = false;
            }
            unregisterReceiver(mBroadcastReceiver4);
            Intent nintent = new Intent(Lobby2.this, NameSubmit.class);
            startActivity(nintent);
            LocalBroadcastManager.getInstance(Lobby2.this).unregisterReceiver(mReceiver);
           /* }*/

        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4

                }
                //case2: creating a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");

                    //mBTDevice = mDevice;

                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                        Log.d(TAG, "Trying to pair with " + "a Device");
                        mDevice.createBond();
                        if(!ConnectedDevices.contains(mDevice)) {

                            //Toast.makeText(getApplicationContext(), "WE Are Here!!", Toast.LENGTH_LONG).show();
                            ConnectedDevices.add(mDevice);
                            BluetoothConnectionService mBluetoothConnection = new BluetoothConnectionService(Lobby2.this);
                            CH.Connections.add(mBluetoothConnection);
                        }
                    }
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");

                    //mBTDevice = mDevice;

                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                        Log.d(TAG, "Trying to pair with " + "a Device");
                        mDevice.createBond();
                        //mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby2);

        ConnectedDevices = new ArrayList<>();
        mBTDevices = new ArrayList<>();
        //Connections = new ArrayList<>();

        reg2 = false;

        CH = ConnectionHolder.getInstance();
        CH.Connections.clear();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
        reg2 = true;
    }
}
