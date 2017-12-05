package com.example.christoffer.mobiletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseBetter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_better);

        ScoreBoard SB = ScoreBoard.getInstance();

        Button button0 = (Button) findViewById(R.id.BetterButton0);
        Button button1 = (Button) findViewById(R.id.BetterButton1);
        Button button2 = (Button) findViewById(R.id.BetterButton2);
        Button button3 = (Button) findViewById(R.id.BetterButton3);
        Button button4 = (Button) findViewById(R.id.BetterButton4);

        if(SB.activePlayer == 0 || SB.Players.get(0).bet != 0)
        {
            button0.setVisibility(View.GONE);
        }
        else
        {
            button0.setText(SB.Players.get(0).name);
        }
        if(SB.activePlayer == 1 || SB.Players.get(1).bet != 0)
        {
            button1.setVisibility(View.GONE);
        }
        else
        {
            button1.setText(SB.Players.get(1).name);
        }
        if(SB.activePlayer == 2 || SB.nrOfPlayers <= 2 || SB.Players.get(2).bet != 0)
        {
            button2.setVisibility(View.GONE);
        }
        else
        {
            button2.setText(SB.Players.get(2).name);
        }
        if(SB.activePlayer == 3 || SB.nrOfPlayers <= 3 || SB.Players.get(3).bet != 0)
        {
            button3.setVisibility(View.GONE);
        }
        else
        {
            button3.setText(SB.Players.get(3).name);
        }
        if(SB.activePlayer == 4 || SB.nrOfPlayers <= 4 || SB.Players.get(4).bet != 0)
        {
            button4.setVisibility(View.GONE);
        }
        else
        {
            button4.setText(SB.Players.get(4).name);
        }
    }

    public void Player0(View view)
    {
        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer" , 0);
        startActivity(intent);
    }
    public void Player1(View view)
    {
        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer" , 1);
        startActivity(intent);
    }
    public void Player2(View view)
    {
        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer" , 2);
        startActivity(intent);
    }
    public void Player3(View view)
    {
        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer" , 3);
        startActivity(intent);
    }
    public void Player4(View view)
    {
        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer" , 4);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DialogBox Dbox = new DialogBox();
        Dbox.show(getSupportFragmentManager(), "Dialog");
    }
}
