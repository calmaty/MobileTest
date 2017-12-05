package com.example.christoffer.mobiletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.R.attr.max;

public class PlayerSelect extends AppCompatActivity {

    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        // you should define max in xml, but if you need to do this by code, you must set max as 0 and then your desired value. this is because a bug in SeekBar (issue 12945) (don't really checked if it was corrected)
        seekBar.setMax(3);
        seekBar.setProgress(0);
        final TextView seekBarValue = (TextView)findViewById(R.id.PlayersText);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress + 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void ConfirmClick(View view) {

        ScoreBoard SB = ScoreBoard.getInstance();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        int value = seekBar.getProgress();
        SB.nrOfPlayers = value + 2;
        SB.WinningPoint = 10;
        Intent intent = new Intent(this, PlayerName.class);
        startActivity(intent);
    }
}
