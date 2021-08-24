package edu.northwestern.threeninethree.backgammon;

import java.util.*;
import java.util.stream.Collectors;

public class Round {

    /**
     * INSTANCE FIELDS
     */
    private final Map<Game, GameResult> games;

    /**
     * CONSTRUCTOR
     */
    public Round(List<Game> games){
        this.games = new LinkedHashMap<>();
        for(Game g : games) this.games.put(g, new GameResult());
    }

    /**
     * GETTER FUNCTIONS
     */

    public GameResult getResult(int i) {
        return new ArrayList<>(this.games.values()).get(i);
    }

    public GameResult getResult(Game g) {
        return this.games.get(g);
    }

    public List<Game> getGames(){
        return new ArrayList<>(this.games.keySet());
    }


    public void add(Game g){
        this.games.put(g, new GameResult());
    }

    public void add(Game g, GameResult gr){
        this.games.put(g, gr);
    }

    public int length(){
        return this.games.keySet().size();
    }
}
