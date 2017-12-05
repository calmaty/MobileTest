package com.example.christoffer.mobiletest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StartBTGame extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;

    private static final String TAG = "StartBTGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_btgame);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
    }

    public void hostGame(View view)
    {
        Intent intent = new Intent(this, Lobby.class);
        startActivity(intent);
    }

    public void joinGame(View view)
    {
        Intent intent = new Intent(this, Lobby2.class);
        startActivity(intent);
    }
}
