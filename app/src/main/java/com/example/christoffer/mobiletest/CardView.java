package com.example.christoffer.mobiletest;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CardView extends AppCompatActivity {

    DBReader mDbHelper;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        ScrollView sv = new ScrollView(this);
        setContentView(sv);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        sv.addView(linearLayout);

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
                DB.DBEntry._ID,
        };

        Cursor cursor = db.query(
                DB.DBEntry.CARD,                          // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                final String Question = cursor.getString(
                        cursor.getColumnIndex(DB.DBEntry.QUESTION));

                TextView textView1 = new TextView(this);
                textView1.setText(Question);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                linearLayout.addView(textView1);

                String AnswerA = cursor.getString(
                        cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_A));
                TextView textView2 = new TextView(this);
                textView2.setText(AnswerA);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                linearLayout.addView(textView2);

                String AnswerB = cursor.getString(
                        cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_B));
                TextView textView3 = new TextView(this);
                textView3.setText(AnswerB);
                textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                linearLayout.addView(textView3);

                String AnswerC = cursor.getString(
                        cursor.getColumnIndexOrThrow(DB.DBEntry.ANSWER_C));
                TextView textView4 = new TextView(this);
                textView4.setText(AnswerC);
                textView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                linearLayout.addView(textView4);

                Button b = new Button(this);
                b.setText("Delete");

                final long Id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(DB.DBEntry._ID));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.delete(DB.DBEntry.CARD, DB.DBEntry._ID + "=" + Id , null);

                        finish();
                        startActivity(getIntent());
                    }
                });

                linearLayout.addView(b);

                TextView textView5 = new TextView(this);
                textView5.setText("-------------------------------");
                textView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                linearLayout.addView(textView5);

                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
