package com.example.christoffer.mobiletest;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created by Christoffer on 21-08-2017.
 */

public class QuestionCard implements Serializable {

    //static final long serialVersionUID = 123456789123456789L;

    public String Question;
    public String AnswerA;
    public String AnswerB;
    public String AnswerC;

    //public String Player;

    public  QuestionCard(String Question, String AnswerA, String AnswerB, String AnswerC)
    {
        this.Question = Question;
        this.AnswerA = AnswerA;
        this.AnswerB = AnswerB;
        this.AnswerC = AnswerC;
    }
}
