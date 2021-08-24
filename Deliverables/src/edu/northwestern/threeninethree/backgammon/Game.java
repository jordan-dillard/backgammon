package edu.northwestern.threeninethree.backgammon;

import java.util.*;

public class Game {

    private final Admin a;
    private final PlayerInterface p1;
    private final PlayerInterface p2;
    private final Map<PlayerInterface, Boolean> hasCheated;

    public Game(PlayerInterface p1, PlayerInterface p2){
        this.a = new Admin(p1, p2, false);
        this.p1 = p1;
        this.p2 = p2;
        this.hasCheated = new HashMap<>();
        this.hasCheated.put(this.p1, false);
        this.hasCheated.put(this.p2, false);
    }

    public PlayerInterface getPlayerOne(){
        return this.p1;
    }

    public PlayerInterface getPlayerTwo(){
        return this.p2;
    }

    public PlayerInterface getOtherPlayer(PlayerInterface p){
        if(p.equals(this.p1)) return this.p2;
        else return this.p1;
    }

    public boolean contains(PlayerInterface p){
        return p.equals(this.p1) || p.equals(this.p2);
    }

    public GameResult play(){
        return this.a.runGame();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Game)) return false;
        else{
            Game g = (Game) o;
            PlayerInterface g1p1 = this.p1;
            PlayerInterface g1p2 = this.p2;
            PlayerInterface g2p1 = g.getPlayerOne();
            PlayerInterface g2p2 = g.getPlayerTwo();
            return ((g1p1.equals(g2p1) && g1p2.equals(g2p2)) // each game's p1 and p2 are the same
                    || (g1p1.equals(g2p2) && g1p2.equals(g2p1))) //
                    && !g1p1.equals(g1p2) // both of the first game's players are not the same
                    && !g2p1.equals(g2p2); // both of the second game's players are not the same
        }
    }

    @Override
    public String toString(){
        String game = "";
        try{
            game = "Player One: " + this.p1.name() + "\nPlayer Two: " + this.p2.name();
        }
        catch(CheatingException ignored){

        }
        return game;
    }
}