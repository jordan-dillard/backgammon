package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleElimTournament extends Tournament {

    /**
     * CONSTRUCTORS
     */
    public SingleElimTournament(List<PlayerInterface> players) {
        super(players);
        if(players.size() >= 2) {
            for (int i = 0; i < players.size(); i += 2) {
                PlayerInterface p1 = players.get(i);
                PlayerInterface p2 = players.get(i + 1);
                this.getGameSchedule().add(new Game(p1, p2));
            }
        }
    }

    /**
     * FUNCTIONS
     */
    @Override
    public String results(){
        List<PlayerInterface> players = this.getPlayers();
        Map<PlayerInterface, Record> scores = this.getScores();
        PlayerInterface winner = players.get(0);
        int maxWins = Integer.MIN_VALUE;
        for(PlayerInterface p : players){
            int currWins = scores.get(p).getWins();
            if(currWins >= maxWins){
                maxWins = currWins;
                winner = p;
            }
        }

        try {
            if(super.isFillerPlayer(winner) || winner.hasCheated()) return JSONValue.toJSONString(false);
            else return JSONValue.toJSONString(winner.name());
        } catch (CheatingException ignore) {
            throw new RuntimeException("No player should logically cheat upon a name request when calculating tournament results.");
        }
    }

    @Override
    public void updateSchedule(){
        List<Round> rounds = this.getGameHistory();
        Round latest = rounds.get(rounds.size()-1);
        if(latest.length() != 1){
            //System.out.println("\n\n NEW ROUND \n\n");
            List<Game> games = new ArrayList<>();
            for(int i = 0; i < latest.length(); i+=2){
                PlayerInterface p1 = latest.getResult(i).getWinner();
                PlayerInterface p2 = latest.getResult(i+1).getWinner();
                games.add(new Game(p1, p2));
            }
            rounds.add(new Round(games));
            this.getGameSchedule().addAll(games);
        }
    }
}
