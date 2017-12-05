package com.example.christoffer.mobiletest;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PlayerName extends AppCompatActivity {


    List<Player> Players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);
        //Players = new ArrayList<Player>();
    }

    public void addPlayer(View view)
    {
        ScoreBoard SB = ScoreBoard.getInstance();
        TextView nameText = (TextView)findViewById(R.id.NameSubmitText);
        TextView playerText = (TextView)findViewById(R.id.PlayerText);
        String name = nameText.getText().toString();
        //Player player = new Player(name);
        //Players.add(player);
        SB.addPlayer(name);
        playerText.setText("Spiler " + (SB.Players.size() + 1) + " skriv dit name");
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, name + " Added", duration);
        toast.show();

        if(SB.stop())
        {
            //SB.addPlayers(Players);
            SB.StartGame();
            Intent intent = new Intent(this, TakeTurn.class);
            startActivity(intent);
        }
        else
        {
            //Toast toast1 = Toast.makeText(context, (Players.get(0).name + "" ), duration);
            //toast1.show();
        }
    }

    @Override
    public void onBackPressed() {
        DialogBox Dbox = new DialogBox();
        Dbox.show(getSupportFragmentManager(), "Dialog");
    }
}
