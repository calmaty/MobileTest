package com.example.christoffer.mobiletest;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 21-09-2017.
 */

public class DTO implements Serializable {

    static final long serialVersionUID = 123456789123456789L;

    public QuestionCard QC;
    public String Name;
    public int Bet;
    public int Choice;
    int correctAnswer;
    int winningPoint;

    public List<Player> Players = new ArrayList<Player>();

    public DTO(String Name)
    {
        this.Name = Name;
    }

    public DTO(QuestionCard QC, String Name)
    {
        this.QC = QC;
        this.Name = Name;
    }

    public DTO(String Name, int Bet, int Choice)
    {
        this.Name = Name;
        this.Bet = Bet;
        this.Choice = Choice;
    }

    public DTO(List<Player> Players, int correctAnswer) {
        this.Players = Players;
        this.correctAnswer = correctAnswer;
    }

    public DTO(int winningPoint) {
        this.winningPoint = winningPoint;
    }
}
