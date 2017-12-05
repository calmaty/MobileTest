package com.example.christoffer.mobiletest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Christoffer on 18-08-2017.
 */

public final class DB {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DB() {}

    /* Inner class that defines the table contents */
    public static class DBEntry implements BaseColumns {
        public static final String CARD = "card";
        public static final String QUESTION = "question";
        public static final String ANSWER_A = "answerA";
        public static final String ANSWER_B = "answerB";
        public static final String ANSWER_C = "answerC";


    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBEntry.CARD + " (" +
                    DBEntry._ID + " INTEGER PRIMARY KEY," +
                    DBEntry.QUESTION + " TEXT," +
                    DBEntry.ANSWER_A + " TEXT," +
                    DBEntry.ANSWER_B + " TEXT," +
                    DBEntry.ANSWER_C + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBEntry.CARD;

    public static String CreateEntrys()
    {
        return SQL_CREATE_ENTRIES;
    }

    public static String DeleteEntrys()
    {
        return SQL_DELETE_ENTRIES;
    }


}
