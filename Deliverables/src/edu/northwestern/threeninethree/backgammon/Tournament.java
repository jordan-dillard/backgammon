package edu.northwestern.threeninethree.backgammon;

import java.util.*;

public class Tournament implements TournamentInterface{

    /**
     * INSTANCE FIELDS
     */
    private final List<PlayerInterface> players;
    private final Map<PlayerInterface, Record> scores;
    private final List<Game> gameSchedule;
    private final List<Round> gameHistory;

    /**
     * CONSTRUCTOR
     */
    public Tournament(List<PlayerInterface> players){
        this.players = players;
        this.gameSchedule = new ArrayList<>();
        this.gameHistory = new ArrayList<>();
        this.gameHistory.add(new Round(this.gameSchedule));
        this.scores = new HashMap<>();
        for(PlayerInterface p : this.players) this.scores.put(p, new Record());
    }

    /**
     * GETTER FUNCTIONS
     */
    public List<Game> getGameSchedule(){
        return this.gameSchedule;
    }

    public List<Round> getGameHistory(){
        return this.gameHistory;
    }

    public Map<PlayerInterface, Record> getScores(){
        return this.scores;
    }

    public Round getLastRound(){
        return this.gameHistory.get(this.gameHistory.size()-1);
    }


    public List<PlayerInterface> getPlayers(){
        return this.players;
    }


    public GameResult getResult(Game g){
        GameResult result = null;
        for(Round r : this.gameHistory){
            for(Game pastG : r.getGames()){
                if(g.equals(pastG)) result = r.getResult(pastG);
            }
        }
        return result;
    }

    /**
     * PREDICATE FUNCTIONS
     */


    public boolean isFillerPlayer(PlayerInterface p){
        boolean isFiller = false;
        try{
            isFiller = p.name().contains("Filler");
        }
        catch(CheatingException ignored){

        }
        return isFiller;
    }

    public boolean hasCheatingWinner(Game g, PlayerInterface p){
        if(this.getResult(g).hasWinner()) return this.getResult(g).getWinner().equals(p);
        else return false;
    }

    /**
     * OTHER FUNCTIONS
     */

    public void run() {
        while(!this.gameSchedule.isEmpty()){
            GameResult results = this.playNext();
            this.rectifyCheating(results);
            this.updateScore(results);
            if(this.gameSchedule.isEmpty()){
                this.updateSchedule();
            }
        }
    }

    public GameResult playNext(){
        Game g = this.gameSchedule.remove(0);
        GameResult gr = g.play();

        this.getLastRound().add(g, gr);
        return gr;
    }

    public void rectifyCheating(GameResult gr){
        List<PlayerInterface> cheaters = gr.getCheaters();
        for(PlayerInterface c : cheaters){
            // handle cheating from past games involving each player

            for(Game pastG : this.getLastRound().getGames()){
                if(this.hasCheatingWinner(pastG, c)) this.rectifyPastCheating(pastG, c);
            }
            // handle future games involving each player in-advance
            for(int i = this.gameSchedule.size()-1; i >= 0; i--){
                Game futureG = this.gameSchedule.get(i);
                if(futureG.contains(c)){
                    this.rectifyFutureCheating(futureG, c);
                    this.gameSchedule.remove(i);
                }
            }
        }
    }

    public void rectifyPastCheating(Game pastG, PlayerInterface cheater){
        GameResult gr = this.getResult(pastG);
        if(gr.getWinner().equals(cheater)) this.scores.get(cheater).forfeitWin();
    }

    public void rectifyFutureCheating(Game futureG, PlayerInterface cheater){
        PlayerInterface nonCheater = futureG.getOtherPlayer(cheater);
        GameResult newGR = new GameResult(nonCheater, cheater);
        this.updateScore(newGR);
        newGR.addCheater(cheater);
        this.gameHistory.get(this.gameHistory.size()-1).add(futureG, newGR);
    }

    public void updateScore(GameResult gr){
        if(gr.hasWinner()) {
            PlayerInterface winner = gr.getWinner();
            this.scores.get(winner).addWin();
        }
        List<PlayerInterface> losers = gr.getLosers();
        for(PlayerInterface loser : losers) this.scores.get(loser).addLoss();
    }

    public void updateSchedule(){
        throw new UnsupportedOperationException();
    }

    public String results() {
        throw new UnsupportedOperationException();
    }
}
