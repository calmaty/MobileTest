package com.example.christoffer.mobiletest;

import android.Manifest;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

public class Lobby extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Lobby";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;


    boolean done;

    boolean reg2;
    boolean reg3;

    ConnectionHolder CH;

    public ArrayList<BluetoothDevice> ConnectedDevices;

    public ArrayList<BluetoothDevice> mBTDevices;

    BluetoothAdapter mBluetoothAdapter;

    //public ArrayList<BluetoothConnectionService> Connections;

    //BluetoothConnectionService mBluetoothConnection;

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;

    TextView NrText;

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

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
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
            if(reg3) {
                unregisterReceiver(mBroadcastReceiver3);
                reg3 =false;
            }
                unregisterReceiver(mBroadcastReceiver4);
                Intent nintent = new Intent(Lobby.this, NameSubmit.class);
                startActivity(nintent);
                done = true;
                LocalBroadcastManager.getInstance(Lobby.this).unregisterReceiver(mReceiver);
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
                            BluetoothConnectionService mBluetoothConnection = new BluetoothConnectionService(Lobby.this);
                            CH.Connections.add(mBluetoothConnection);
                        }
                    }

                    NrText.setText("Players Added: " + CH.Connections.size());
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
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        //unregisterReceiver(mBroadcastReceiver1);
        if (reg2) {
            unregisterReceiver(mBroadcastReceiver2);
            reg2 = false;
        }
        if(reg3) {
            unregisterReceiver(mBroadcastReceiver3);
            reg3 =false;
        }
        //unregisterReceiver(mBroadcastReceiver4);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        //mBluetoothAdapter.cancelDiscovery();
      /*  if(CH != null) {
            for(BluetoothConnectionService conection : CH.Connections)
            {
                conection.Terminate();
            }
            CH.Connections.clear();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        ConnectedDevices = new ArrayList<>();
        mBTDevices = new ArrayList<>();
        //Connections = new ArrayList<>();

        reg2 = false;
        reg3 = false;

        CH = ConnectionHolder.getInstance();
        CH.Connections.clear();

        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        lvNewDevices.setOnItemClickListener(Lobby.this);

        NrText = (TextView)findViewById(R.id.NrText);

        btnDiscover();
    }

    //create method for starting connection
    //***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(View view){
        startBTConnection(MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        int i = 0;
        for(BluetoothConnectionService conection : CH.Connections)
        {
            conection.startClient(ConnectedDevices.get(i),uuid);
            i++;

            //Toast.makeText(getApplicationContext(), "" + i, Toast.LENGTH_LONG).show();
        }

        //CH.Connections = Connections;

        done = true;

        LocalBroadcastManager.getInstance(Lobby.this).unregisterReceiver(mReceiver);
        if (reg2) {
            unregisterReceiver(mBroadcastReceiver2);
            reg2 = false;
        }
        if(reg3) {
            unregisterReceiver(mBroadcastReceiver3);
            reg3 =false;
        }
        unregisterReceiver(mBroadcastReceiver4);

        Intent intent = new Intent(this, RuleSelect.class);
        startActivity(intent);
    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
        reg2 = true;

    }

    public void btnDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        mBTDevices.clear();

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
            reg3 = true;

        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            //checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
            reg3 = true;
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        Toast.makeText(getApplicationContext(), "Please Wait for devices to pair", Toast.LENGTH_LONG).show();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            unpairDevice(mBTDevices.get(i));
            mBTDevices.get(i).createBond();
            //mBTDevice = mBTDevices.get(i);
            //mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
