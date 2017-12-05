package com.example.christoffer.mobiletest;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Christoffer on 04-08-2017.
 */

public class ScoreBoard {
    static ScoreBoard ourInstance = new ScoreBoard();

    public static ScoreBoard getInstance() {
        return ourInstance;
    }
    List<Player> Players;
    QuestionCard QC;
    int nrOfPlayers;
    int activePlayer;
    int CorrectAnswer;
    int bet;
    int WinningPoint;
    boolean Host;
    Boolean BT;
    String MyName;
    String BTName;

    private ScoreBoard() {
        Players = new ArrayList<Player>();
        Host = false;
        BT = false;
        WinningPoint = 99;
    }

    public void addPlayer(String name) {
        Player p = new Player(name);
        Players.add(p);
    }

    public void addPlayers(List<Player> playerList) {
        Players = playerList;
    }
    public void nrOfPlayers(int number)
    {
        nrOfPlayers = number;
    }

    public boolean stop()
    {
        boolean stop = false;

        if (nrOfPlayers <= Players.size())
        {
            stop = true;
        }
        return stop;
    }

    public void StartGame()
    {
        Random random = new Random();
        activePlayer = random.nextInt(nrOfPlayers);
    }

    public void Answer(int correctAnswer, int bet)
    {
        this.CorrectAnswer = correctAnswer;
        this.bet = bet;
        Players.get(activePlayer).bet = bet;
        Players.get(activePlayer).choice = correctAnswer;
    }

    public void Bet(int better, int answer, int bet)
    {
        Players.get(better).choice = answer;
        Players.get(better).bet = bet;
    }

    public void passTurn()
    {
        CorrectAnswer = 0;
        if(activePlayer == nrOfPlayers -1)
        {
            activePlayer = 0;
        }
        else
        {
            activePlayer++;
        }

        for(Player p : Players)
        {
            p.bet = 0;
            p.choice = 0;
        }
    }

    public void GameOver()
    {
        Players.clear();
        BT = false;
        Host = false;

    }

    public void Reset ()
    {
        for(Player p : Players)
        {
            p.points = 0;
        }
    }

    public String GetActivePlayerName()
    {
        return Players.get(activePlayer).name;
    }

    public Player GetPlayerbyName(String name)
    {
        Player player = null;

        for(Player p : Players)
        {
            if(p.name.equals(name))
            {
                return p;
            }
        }

        return player;
    }
}
