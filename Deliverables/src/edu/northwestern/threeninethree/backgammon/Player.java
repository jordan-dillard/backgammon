package edu.northwestern.threeninethree.backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class Player implements PlayerInterface{

    /**
     * INSTANCE FIELDS
     */
    private final String name;
    private Color color;
    private boolean activeGame;

    private UUID pid;

    /**
     * CONSTRUCTORS
     */

    public Player(String tmpName, Color tmpColor){
        this.name = tmpName;
        this.color = tmpColor;
        this.activeGame = false;
        this.pid = null;
    }

    public Player(String tmpName){
        this.name = tmpName;
        this.color = null;
        this.activeGame = false;
        this.pid = null;
    }

    public Player(String tmpName, UUID tmpPid){
        this.name = tmpName;
        this.color = null;
        this.activeGame = false;
        this.pid = tmpPid;
    }

    /**
     * GETTERS
     */
    public String name() {
        return this.name;
    }

    public Color color() {
        return this.color;
    }

    public UUID getId() { return this.pid;}

    /**
     * SETTERS
     */
    public void setGameState(boolean b){
        this.activeGame = b;
    }

    /**
     * PREDICATES
     */
    public boolean isGameActive(){
        return this.activeGame;
    }

    public boolean hasCheated() { return false;}

    public boolean hasCheatedWithName() { return false;}

    /**
     * OTHER FUNCTIONS
     */
    public void startGame(Color c, String oppName) {
        this.color = c;

        /**
        System.err.println("The game of Backgammon has begun!");
        System.err.println(this.name + "'s color is: " + Color.denumString(this.color));
        System.err.println(this.name + "'s opponent is: " + oppName);
         */
    }

    public Turn turn(BoardInterface b, List<Integer> dice) {
        throw new UnsupportedOperationException();
    }

    public void endGame(BoardInterface b, boolean hasWon) {
        String playerResult;
        if(hasWon) playerResult = "won!";
        else playerResult = "lost.";

        /**
        System.err.println("Game over.");
        System.err.println(b.toJSON());
        System.err.println(this.name + " has " + playerResult);
         */
    }
}
