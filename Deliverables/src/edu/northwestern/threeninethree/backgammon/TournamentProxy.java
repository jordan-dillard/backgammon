package edu.northwestern.threeninethree.backgammon;

import java.util.List;

public class TournamentProxy implements TournamentInterface{

    /**
     * INSTANCE FIELDS
     */
    private final TournamentInterface t;

    /**
     * CONSTRUCTOR
     */
    public TournamentProxy(List<PlayerInterface> players, TournamentType type){
        switch(type){
            case ROUND_ROBIN: this.t = new RoundRobinTournament(players);
            break;
            case SINGLE_ELIMINATION: this.t = new SingleElimTournament(players);
            break;
            default: throw new RuntimeException("Invalid tournament type: " + type);
        }
    }

    /**
     * FUNCTIONS
     */
    public void run() {
        this.t.run();
    }

    public String results() {
        return this.t.results();
    }
}
