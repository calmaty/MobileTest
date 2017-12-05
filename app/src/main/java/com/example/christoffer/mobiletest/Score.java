package com.example.christoffer.mobiletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Score extends AppCompatActivity implements View.OnClickListener {

    Boolean GameOver;
    ScoreBoard SB;
    Boolean Cheecked;
    Button b;

    ConnectionHolder CH;
    DBReader mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(CH != null) {
            for(BluetoothConnectionService conection : CH.Connections)
            {
                conection.Terminate();
            }
            CH.Connections.clear();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(turnReceiver);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SB = ScoreBoard.getInstance();

        if(SB.BT)
        {
            Cheecked = false;
            CH = ConnectionHolder.getInstance();
            LocalBroadcastManager.getInstance(this).registerReceiver(turnReceiver, new IntentFilter("incomingMessage"));
        }

        List<Player> unrankedPlayers = new ArrayList<Player>(SB.Players);
        List<Player> rankedPlayers = new ArrayList<Player>();
        GameOver = false;

        for (int i = 0 ; i < unrankedPlayers.size();)
        {
            int highest = -999;
            Player highestPlayer = new Player("1");

            for (Player p : unrankedPlayers) {
                if(p.points > highest)
                {
                    highest = p.points;
                    highestPlayer = p;
                }
            }
            unrankedPlayers.remove(highestPlayer);
            rankedPlayers.add(highestPlayer);
            if (highestPlayer.points >= SB.WinningPoint)
            {
                GameOver = true;
            }
        }

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.GRAY);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView PT = new TextView(this);
        linearLayout.addView(PT);
        if(SB.BT)
        {
            if(SB.BTName.equals(SB.MyName))
            {
                PT.setText("Du svarede");
            }
            else
            {
                PT.setText(SB.BTName + " svarede");
            }
        }
        else
        {
            PT.setText(SB.GetActivePlayerName() + " svarede:");
        }

        PT.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
        TextView QT = new TextView(this);
        int CA = SB.CorrectAnswer;
        switch (CA) {
            case 1:
                QT.setText(SB.QC.AnswerA);
                break;
            case 2:
                QT.setText(SB.QC.AnswerB);
                break;
            case 3:
                QT.setText(SB.QC.AnswerC);
                break;
        }
        QT.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        linearLayout.addView(QT);

        TextView textView = new TextView(this);
        if(GameOver)
        {
            if(SB.BT && SB.WinningPoint <= SB.GetPlayerbyName(SB.MyName).points)
            {
                textView.setText("Du vandt!!!");
                textView.setTextColor(Color.GREEN);
            }
            else if(SB.BT)
            {
                textView.setText("Du tabte!");
                textView.setTextColor(Color.RED);
            }
            else
            {
                textView.setText("Game Over!");
            }

        }
        else
        {
            textView.setText("Score");
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        linearLayout.addView(textView);

        for(Player p : rankedPlayers)
        {
            TextView textView1 = new TextView(this);
            textView1.setText(p.name + ": " + p.symbol + " " + p.bet + " - Total Score: " + p.points + " points!");
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            if(p.symbol == 'X')
            {
                textView1.setTextColor(Color.RED);
            }
            else
            {
                textView1.setTextColor(Color.GREEN);
            }

            linearLayout.addView(textView1);
        }

        b = new Button(this);
        if(GameOver)
        {
            b.setText("Tilbage til Title");
            Button b2 = new Button(this);
            b2.setText("Nyt spil");
            b2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //SB.passTurn();
                    SB.Reset();
                    GameOver = false;
                    NewTurn();
                }
            });
            linearLayout.addView(b2);
        }
        else
        {
            b.setText("Næste runde");
        }
        //b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(b);

        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       NewTurn();
    }

    public void NewTurn()
    {
        SB.passTurn();

        if(GameOver)
        {
            if(SB.BT) {
                for(BluetoothConnectionService conection : CH.Connections)
                {
                   conection.Terminate();
                }
                CH.Connections.clear();

                LocalBroadcastManager.getInstance(Score.this).unregisterReceiver(turnReceiver);
            }

            SB.GameOver();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            if(SB.BT)
            {
                if(SB.Host)
                {
                    //lot of Database stuff
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

                    //Toast.makeText(getApplicationContext(), dto.Name +" Sending Question", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, Question.class);
                    startActivity(intent);
                    LocalBroadcastManager.getInstance(this).unregisterReceiver(turnReceiver);
                }
                else if(Cheecked == true)
                {
                    Intent intent = new Intent(this, Question.class);
                    startActivity(intent);
                    LocalBroadcastManager.getInstance(this).unregisterReceiver(turnReceiver);
                }
                else
                {
                    Cheecked = true;
                    b.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "venter på andre spillere", Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Intent intent = new Intent(this, TakeTurn.class);
                startActivity(intent);
            }

        }
    }

    BroadcastReceiver turnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Object qco = intent.getSerializableExtra("dto");

            DTO dto = (DTO) qco;

            SB.QC = dto.QC;
            SB.BT = true;
            SB.BTName = dto.Name;

            //Toast.makeText(getApplicationContext(), dto.Name +" Recieving Question", Toast.LENGTH_LONG).show();

            if(Cheecked == true)
            {
                Intent nintent = new Intent(Score.this, Question.class);
                startActivity(nintent);
            }
            else
            {
                Cheecked = true;
            }
            LocalBroadcastManager.getInstance(Score.this).unregisterReceiver(turnReceiver);

        }
    };

    @Override
    public void onBackPressed() {
        DialogBox Dbox = new DialogBox();
        Dbox.show(getSupportFragmentManager(), "Dialog");
    }

}
