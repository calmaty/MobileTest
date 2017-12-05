package com.example.christoffer.mobiletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class NameSubmit extends AppCompatActivity {

    ConnectionHolder CH;
    DBReader mDbHelper;
    SQLiteDatabase db;

    ScoreBoard SB;

    EditText PT;

    Button SubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_submit);

        CH = ConnectionHolder.getInstance();
        SB = ScoreBoard.getInstance();

        PT = (EditText)findViewById(R.id.NameSubmitText);

        SubmitButton = (Button)findViewById(R.id.SubmitButton);

        for(BluetoothConnectionService conection : CH.Connections)
        {
            conection.mContext = NameSubmit.this;
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(nameReceiver, new IntentFilter("incomingMessage"));
    }

    public void SubmitName(View view)
    {
        if(SB.Host == true)
        {
            SB.addPlayer(PT.getText().toString());
            if(SB.Players.size() == CH.Connections.size() +1)
            {
                SB.nrOfPlayers = SB.Players.size();
                SB.StartGame();
                StartGame();
            }
        }
        else
        {
            //QuestionCard QC = new QuestionCard("","","","");
            DTO dto = new DTO(PT.getText().toString());
            //QC.Player = PT.getText().toString();
            for(BluetoothConnectionService conection : CH.Connections)
            {
                conection.writeSe(dto);
            }
        }

        SB.MyName = PT.getText().toString();
        SubmitButton.setEnabled(false);
        Toast.makeText(getApplicationContext(), "Navn Send", Toast.LENGTH_LONG).show();
    }

    BroadcastReceiver nameReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object qco = intent.getSerializableExtra("dto");

            DTO dto = (DTO) qco;

            if(SB.Host)
            {
                SB.addPlayer(dto.Name);
                //Toast.makeText(getApplicationContext(), dto.Name +" submited his/her name", Toast.LENGTH_LONG).show();
                if(SB.Players.size() == CH.Connections.size() +1)
                {
                    //Toast.makeText(getApplicationContext(), "Done!!!", Toast.LENGTH_LONG).show();
                    SB.nrOfPlayers = SB.Players.size();
                    SB.StartGame();
                    StartGame();
                }
            }
            else
            {
                SB.QC = dto.QC;
                SB.BT = true;
                SB.BTName = dto.Name;
                Intent nintent = new Intent(NameSubmit.this, Question.class);
                startActivity(nintent);
                LocalBroadcastManager.getInstance(NameSubmit.this).unregisterReceiver(nameReceiver);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(nameReceiver);
        /*if(CH != null) {
            for(BluetoothConnectionService conection : CH.Connections)
            {
                conection.Terminate();
            }
            CH.Connections.clear();
        }*/
    }

    public void StartGame()
    {
        mDbHelper = new DBReader(this);

        // Gets the data repository in write mode
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DB.DBEntry.QUESTION,
                DB.DBEntry.ANSWER_A,
                DB.DBEntry.ANSWER_B,
                DB.DBEntry.ANSWER_C,
        };

        int cnt  = (int) DatabaseUtils.queryNumEntries(db, DB.DBEntry.CARD);

        Random random = new Random();
        int RandomID = random.nextInt(cnt) +1;

        Cursor cursor = db.query(DB.DBEntry.CARD, projection, DB.DBEntry._ID + "=" + RandomID , null, null, null, null);

        while(cursor.moveToNext()) {
            String Qustion = cursor.getString(
                    cursor.getColumnIndexOrThrow(DB.DBEntry.QUESTION));
            String AnswerA = cursor.getString(
                    cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_A));
            String AnswerB = cursor.getString(
                    cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_B));
            String AnswerC = cursor.getString(
                    cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_C));

            SB.QC = new QuestionCard(Qustion, AnswerA, AnswerB, AnswerC);

        }
        cursor.close();
        db.close();

        SB.BT = true;
        SB.BTName = SB.GetActivePlayerName();
        DTO dto = new DTO(SB.QC, SB.GetActivePlayerName());

        for(BluetoothConnectionService conection : CH.Connections)
        {
            conection.writeSe(dto);
        }

        Intent intent = new Intent(this, Question.class);
        startActivity(intent);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(nameReceiver);
    }
}