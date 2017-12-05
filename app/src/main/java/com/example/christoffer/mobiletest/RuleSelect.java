package com.example.christoffer.mobiletest;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class RuleSelect extends AppCompatActivity {

    ConnectionHolder CH;

    SeekBar seekBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(CH != null) {
//            for(BluetoothConnectionService conection : CH.Connections)
//            {
//                conection.Terminate();
//            }
//            CH.Connections.clear();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_select);

        CH = ConnectionHolder.getInstance();

        seekBar = (SeekBar) findViewById(R.id.WPSeekBar);
        // you should define max in xml, but if you need to do this by code, you must set max as 0 and then your desired value. this is because a bug in SeekBar (issue 12945) (don't really checked if it was corrected)
        seekBar.setMax(15);
        seekBar.setProgress(0);
        final TextView seekBarValue = (TextView)findViewById(R.id.PointDisplay);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress + 5));
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

    public void send(View view)
    {
        ScoreBoard SB = ScoreBoard.getInstance();
        seekBar = (SeekBar) findViewById(R.id.WPSeekBar);
        int value = seekBar.getProgress();
        SB.WinningPoint = value + 5;
        SB.Host = true;
        for(BluetoothConnectionService conection : CH.Connections)
        {
            conection.writeSe(new DTO(SB.WinningPoint));
        }
        Intent intent = new Intent(this, NameSubmit.class);
        startActivity(intent);
    }
}
