package com.example.christoffer.mobiletest;

import java.io.Serializable;

/**
 * Created by Christoffer on 04-08-2017.
 */

public class Player implements Serializable {

    static final long serialVersionUID = 123456789123456798L;

    public String name;
    public int points;
    public int choice;
    public int bet;
    public char symbol;

    public Player(String name)
    {
        this.name = name;
        points = 0;
        choice = 0;
        bet = 0;
    }
}
