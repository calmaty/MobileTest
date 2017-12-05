package com.example.christoffer.mobiletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Question extends AppCompatActivity {


    private int better;
    ScoreBoard SB;
    Boolean BT;
    ConnectionHolder CH;
    Button betButton;


    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if(CH != null) {
            for(BluetoothConnectionService conection : CH.Connections)
            {
                conection.Terminate();
            }
            CH.Connections.clear();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(AnswerReceiver);
        }*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        SB = ScoreBoard.getInstance();
        BT = SB.BT;

        if(BT)
        {
            CH = ConnectionHolder.getInstance();
            LocalBroadcastManager.getInstance(this).registerReceiver(AnswerReceiver, new IntentFilter("incomingMessage"));
            betButton = (Button)findViewById(R.id.BetButton);
        }
        else {

            Bundle bundle = getIntent().getExtras();
            better = bundle.getInt("BettingPlayer");
        }

        final TextView text = (TextView) findViewById(R.id.QuestionText);
        final TextView text2 = (TextView) findViewById(R.id.PersonText);
        final ToggleButton a = (ToggleButton) findViewById(R.id.AnswerAButton);
        final ToggleButton b = (ToggleButton) findViewById(R.id.AnswerBButton);
        final ToggleButton c = (ToggleButton) findViewById(R.id.AnswerCButton);

        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        if(BT)
        {
            if(SB.BTName.equals(SB.MyName))
            {
                text2.setText("hvad ville du gøre?");
            }
            else
            {
                text2.setText("hvad ville " + SB.BTName + " gøre?");
            }
        }
        else
        {
            if(SB.activePlayer == better)
            {
                text2.setText("hvad ville du gøre?");
            }
            else
            {
                text2.setText("Hvad ville " + SB.GetActivePlayerName() + " gøre?");
            }
        }

        text.setText(SB.QC.Question);
        a.setTextOff(SB.QC.AnswerA);
        a.setText(SB.QC.AnswerA);
        a.setTextOn(SB.QC.AnswerA + "✔");
        b.setText(SB.QC.AnswerB);
        b.setTextOff(SB.QC.AnswerB);
        b.setTextOn(SB.QC.AnswerB + "✔");
        c.setText(SB.QC.AnswerC);
        c.setTextOff(SB.QC.AnswerC);
        c.setTextOn(SB.QC.AnswerC + "✔");

        a.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                b.setChecked(false);
                c.setChecked(false);
            }
        });

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                a.setChecked(false);
                c.setChecked(false);
            }
        });

        c.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                a.setChecked(false);
                b.setChecked(false);
            }
        });
    }

    public void placeBet(View view)
    {
        if(BT)
        {
            PlaceBetBT();
        }
        else
        {
            placeBetNormal();
        }
    }

    public void placeBetNormal()
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        ToggleButton a = (ToggleButton) findViewById(R.id.AnswerAButton);
        ToggleButton b = (ToggleButton) findViewById(R.id.AnswerBButton);
        ToggleButton c = (ToggleButton) findViewById(R.id.AnswerCButton);

        RadioButton one = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton two = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton three = (RadioButton) findViewById(R.id.radioButton3);

        int Choice;
        int Bet;

        if((a.isChecked() || b.isChecked() || c.isChecked()) && (one.isChecked() || two.isChecked() || three.isChecked()))
        {
            if(a.isChecked())
            {
                Choice = 1;
            }
            else if(b.isChecked())
            {
                Choice = 2;
            }
            else
            {
                Choice = 3;
            }

            if(one.isChecked())
            {
                Bet = 1;
            }
            else if(two.isChecked())
            {
                Bet = 2;
            }
            else
            {
                Bet = 3;
            }

            if(SB.activePlayer == better)
            {
                SB.Answer(Choice, Bet);
            }
            else
            {
                SB.Bet(better, Choice, Bet);
            }
            Toast faultToast = Toast.makeText(context, "Bet lavet", duration);
            faultToast.show();

           CheckAndFinsih();
        }
        else
        {
            Toast faultToast = Toast.makeText(context, "Vælg venlist et valg og bet", duration);
            faultToast.show();
        }
    }

    public void PlaceBetBT()
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        ToggleButton a = (ToggleButton) findViewById(R.id.AnswerAButton);
        ToggleButton b = (ToggleButton) findViewById(R.id.AnswerBButton);
        ToggleButton c = (ToggleButton) findViewById(R.id.AnswerCButton);

        RadioButton one = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton two = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton three = (RadioButton) findViewById(R.id.radioButton3);

        int Choice;
        int Bet;

        if((a.isChecked() || b.isChecked() || c.isChecked()) && (one.isChecked() || two.isChecked() || three.isChecked())) {
            if (a.isChecked()) {
                Choice = 1;
            } else if (b.isChecked()) {
                Choice = 2;
            } else {
                Choice = 3;
            }

            if (one.isChecked()) {
                Bet = 1;
            } else if (two.isChecked()) {
                Bet = 2;
            } else {
                Bet = 3;
            }

            if(SB.Host)
            {
                if (SB.BTName.equals(SB.MyName)) {
                    SB.Answer(Choice, Bet);
                } else {
                    Player me = SB.GetPlayerbyName(SB.MyName);
                    me.bet = Bet;
                    me.choice = Choice;
                }

                betButton.setEnabled(false);
                CheckAndFinsih();
            }
            else
            {
                for(BluetoothConnectionService conection : CH.Connections)
                {
                    conection.writeSe(new DTO(SB.MyName, Bet, Choice));
                }
            }

            //LocalBroadcastManager.getInstance(this).unregisterReceiver(AnswerReceiver);
            Toast faultToast = Toast.makeText(context, "Bet Placeret", duration);
            faultToast.show();
            betButton.setEnabled(false);

        }
    }

    public void CheckAndFinsih()
    {
        boolean doneBetting = true;

        for (int i = 0; i < SB.nrOfPlayers; i++)
        {
            if(SB.Players.get(i).choice == 0)
            {
                doneBetting = false;
            }
        }

        if(doneBetting)
        {
            boolean rightAnswer = false;
            for (int i = 0; i < SB.nrOfPlayers; i++)
            {
                if(SB.Players.get(i).choice == SB.CorrectAnswer)
                {
                    if(i != SB.activePlayer)
                    {
                        SB.Players.get(i).points += SB.Players.get(i).bet;
                        SB.Players.get(i).symbol = '✓';
                        rightAnswer = true;
                    }
                }
                else
                {
                    if(i != SB.activePlayer)
                    {
                        SB.Players.get(i).points -= SB.Players.get(i).bet;
                        SB.Players.get(i).symbol = 'X';
                    }
                }
            }

            if (rightAnswer)
            {
                Player ActivePlayer = SB.Players.get(SB.activePlayer);
                ActivePlayer.points += ActivePlayer.bet;
                ActivePlayer.symbol = '✓';
            }
            else
            {
                Player ActivePlayer = SB.Players.get(SB.activePlayer);
                ActivePlayer.points -= ActivePlayer.bet;
                ActivePlayer.symbol = 'X';
            }

            if(SB.BT == true)
            {
                for(BluetoothConnectionService conection : CH.Connections)
                {
                    conection.writeSe(new DTO(SB.Players, SB.CorrectAnswer));
                }
            }
            Intent intent = new Intent(this, Score.class);
            startActivity(intent);
            LocalBroadcastManager.getInstance(Question.this).unregisterReceiver(AnswerReceiver);
        }
        else if(BT == false)
        {
            Intent intent = new Intent(this, ChooseBetter.class);
            startActivity(intent);
        }
    }

    BroadcastReceiver AnswerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object qco = intent.getSerializableExtra("dto");

            DTO dto = (DTO) qco;

            if(SB.Host == true)
            {
                if (SB.BTName.equals(dto.Name)) {
                    SB.Answer(dto.Choice, dto.Bet);
                } else {
                    Player me = SB.GetPlayerbyName(dto.Name);
                    me.bet = dto.Bet;
                    me.choice = dto.Choice;
                }
                CheckAndFinsih();
                //Toast.makeText(getApplicationContext(), dto.Name +" submited his/her Answer", Toast.LENGTH_LONG).show();
            }
            else
            {
                SB.Players = dto.Players;
                SB.CorrectAnswer = dto.correctAnswer;
                Intent nintent = new Intent(Question.this, Score.class);
                startActivity(nintent);
                LocalBroadcastManager.getInstance(Question.this).unregisterReceiver(AnswerReceiver);
            }
        }
    };

    @Override
    public void onBackPressed() {
        DialogBox Dbox = new DialogBox();
        Dbox.show(getSupportFragmentManager(), "Dialog");
    }
}
