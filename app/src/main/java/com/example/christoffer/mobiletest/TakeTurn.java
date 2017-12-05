package com.example.christoffer.mobiletest;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class TakeTurn extends AppCompatActivity {

    DBReader mDbHelper;
    SQLiteDatabase db;
    ScoreBoard SB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_turn);

        SB = ScoreBoard.getInstance();

        Button button = (Button) findViewById(R.id.NameButton);
        button.setText(SB.GetActivePlayerName());

    }

    public void StartTurn(View view)
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

        int cnt  = (int)DatabaseUtils.queryNumEntries(db, DB.DBEntry.CARD);

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

        Intent intent = new Intent(this, Question.class);
        intent.putExtra("BettingPlayer", SB.activePlayer);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DialogBox Dbox = new DialogBox();
        Dbox.show(getSupportFragmentManager(), "Dialog");
    }
}
