package com.example.christoffer.mobiletest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCardActivity extends AppCompatActivity {

    DBReader mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);


    }

    public void AddCard(View view)
    {
        EditText Question = (EditText) findViewById(R.id.QuestionText);
        EditText AnswerA = (EditText) findViewById(R.id.AnswerA);
        EditText AnswerB = (EditText) findViewById(R.id.AnswerB);
        EditText AnswerC = (EditText) findViewById(R.id.AnswerC);


        if(Question.getText().toString() == "" && AnswerA.getText().toString() == "" && AnswerB.getText().toString() == "" && AnswerC.getText().toString() == "")
        {
            mDbHelper = new DBReader(this);

            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DB.DBEntry.QUESTION, Question.getText().toString());
            values.put(DB.DBEntry.ANSWER_A, AnswerA.getText().toString());
            values.put(DB.DBEntry.ANSWER_B, AnswerB.getText().toString());
            values.put(DB.DBEntry.ANSWER_C, AnswerC.getText().toString());

// Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(DB.DBEntry.CARD, null, values);
        }
        else
        {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "alle text fælter skal udfyldes", duration);
            toast.show();
        }


    }

    @Override
    protected void onDestroy() {
        if(mDbHelper != null) {
            mDbHelper.close();
        }
        super.onDestroy();
    }
}
