package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameResult {

    private PlayerInterface winner;
    private List<PlayerInterface> losers;
    private List<PlayerInterface> cheaters;

    public GameResult(){
        this.winner = null;
        this.losers= new ArrayList<>();
        this.cheaters = new ArrayList<>();
    }

    public GameResult(PlayerInterface winner){
        this.winner = winner;
        this.losers = new ArrayList<>();
        this.cheaters = new ArrayList<>();
    }

    public GameResult(PlayerInterface winner, PlayerInterface loser){
        this.winner = winner;
        this.losers = new ArrayList<>();
        this.losers.add(loser);
        this.cheaters = new ArrayList<>();
    }

    public GameResult(List<PlayerInterface> losers){
        this.winner = null;
        this.losers = new ArrayList<>(losers);
        this.cheaters = new ArrayList<>();
    }

    public void addCheater(PlayerInterface p){
        this.cheaters.add(p);
    }

    public PlayerInterface getWinner(){
        return this.winner;
    }

    public List<PlayerInterface> getLosers(){
        return this.losers;
    }

    public List<PlayerInterface> getCheaters(){
        return this.cheaters;
    }

    public JSONObject winnerToJSON(){
        JSONObject adminGameOver = new JSONObject();
        try {
            adminGameOver.put("winner-name", this.winner.name());
        }
        catch(CheatingException ignored){

        }
        return adminGameOver;
    }

    @Override
    public String toString(){
        String results = "";
        try{
            results = "Winner: " + this.winner.name()
                    + "\nLoser(s): " + this.losers.get(0).name();
            if(!this.cheaters.isEmpty()) results += "\nCheater(s): " + this.cheaters.get(0).name();
        }
        catch (CheatingException ignored){

        }
        return results;
    }

    public boolean hasWinner(){
        return this.winner != null;
    }
}
