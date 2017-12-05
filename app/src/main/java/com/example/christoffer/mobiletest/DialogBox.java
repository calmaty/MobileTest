package com.example.christoffer.mobiletest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Christoffer on 30-10-2017.
 */

public class DialogBox extends DialogFragment {

    ScoreBoard SB;
    ConnectionHolder CH;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ConnectionHolder CH = ConnectionHolder.getInstance();
        builder.setMessage("Forlad Spilet?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SB = ScoreBoard.getInstance();
                        if(SB.BT) {
                            CH = ConnectionHolder.getInstance();
                            for(BluetoothConnectionService conection : CH.Connections)
                            {
                                conection.Terminate();
                            }
                            CH.Connections.clear();
                        }

                        SB.GameOver();
                        Intent i= new Intent(getContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getContext().startActivity(i);
                    }
                })
                .setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
