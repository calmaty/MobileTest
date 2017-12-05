package com.example.christoffer.mobiletest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    DBReader mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDbHelper = new DBReader(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Long nr = DatabaseUtils.queryNumEntries(db, DB.DBEntry.CARD);
        if(0  == nr)
        {
            ContentValues values = new ContentValues();
            try {
                InputStream is = getAssets().open("QC.txt");

               BufferedReader reader = new BufferedReader(new InputStreamReader(is, "windows-1252"));
                String line;
                int Count =1;
               while ((line = reader.readLine()) != null)
                {
                    switch (Count) {
                        case 1:
                            values.put(DB.DBEntry.QUESTION, line);
                            break;
                        case 2:
                            values.put(DB.DBEntry.ANSWER_A, line);
                            break;
                        case 3:
                            values.put(DB.DBEntry.ANSWER_B, line);
                            break;
                        case 4:
                            values.put(DB.DBEntry.ANSWER_C, line);
                            long newRowId = db.insert(DB.DBEntry.CARD, null, values);
                            values.clear();
                            Count = 0;
                            break;
                    }

                    Count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        db.close();
    }

    public void onClickNewGame(View view)
    {
        Intent intent = new Intent(this, PlayerSelect.class);
        startActivity(intent);
    }

    public void onClickNewGame2(View view)
    {
        Intent intent = new Intent(this, StartBTGame.class);
        startActivity(intent);
    }

    public void onClickCreateCard(View view)
    {
        Intent intent = new Intent(this, CreateCardActivity.class);
        startActivity(intent);
    }

    public void onClickViewCard(View view)
    {
        Intent intent = new Intent(this, CardView.class);
        startActivity(intent);
    }
}
