package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;

public class PlayerRecordPair {

    private final PlayerInterface p;
    private final Record r;

    public PlayerRecordPair(PlayerInterface p){
        this.p = p;
        this.r = new Record();
    }

    public PlayerRecordPair(PlayerInterface p, Record r){
        this.p = p;
        this.r = r;
    }

    public PlayerInterface getPlayer(){
        return this.p;
    }

    public Record getRecord(){
        return this.r;
    }

    public int getWins(){
        return this.r.getWins();
    }

    public JSONArray toJSON(){
        JSONArray json = new JSONArray();
        try {
            json.add(this.p.name());
            json.add(this.r.getWins());
            json.add(this.r.getLosses());
        } catch (CheatingException ignored) {
        }
        return json;
    }

}