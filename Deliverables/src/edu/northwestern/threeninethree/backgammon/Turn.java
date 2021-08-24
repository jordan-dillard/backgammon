package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Turn{

    /**
     * INSTANCE FIELDS
     */
    private final List<Move> moves;

    /**
     * CONSTRUCTORS
     */
    public Turn(){
        this.moves = new ArrayList<>();
    }

    public Turn(List<Move> moves){
        this.moves = new ArrayList<>(moves);
    }

    /**
     * GETTER FUNCTIONS
     */
    public List<Move> getMoves() {
        return this.moves;
    }

    public int length(){
        return this.moves.size();
    }

    public void addMove(Move m){
        this.moves.add(m);
    }

    public JSONArray toJSON(){
        JSONArray turn = new JSONArray();
        for(Move m : this.moves) turn.add(m.toJSON());
        return turn;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Turn)) return false;
        else {
            Turn t = (Turn) o;
            for (int i = 0; i < this.moves.size(); i++) {
                for (int j = 0; j < t.getMoves().size(); j++) {
                    if (i == j && !(this.moves.get(i).equals(t.getMoves().get(j)))) return false;
                }
            }
            return this.length() == t.length();
        }
    }

    @Override
    public String toString(){
        return "[" + this.moves.stream().map(Move::toString).collect((Collectors.joining(","))) + "]";
    }
}
