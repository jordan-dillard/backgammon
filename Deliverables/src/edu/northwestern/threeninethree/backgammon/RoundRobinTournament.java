package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoundRobinTournament extends Tournament {

    /**
     * CONSTRUCTORS
     */
    public RoundRobinTournament(List<PlayerInterface> players){
        super(players);
        for(PlayerInterface p1 : players){
            for(PlayerInterface p2 : players){
                if(!p1.equals(p2)){
                    Game g = new Game(p1, p2);
                    if(!this.getGameSchedule().contains(g)) this.getGameSchedule().add(g);
                }
            }
        }
    }

    /**
     * GETTER FUNCTIONS
     */
    public List<PlayerRecordPair> getPairs(){
        List<PlayerInterface> players = this.getPlayers();
        Map<PlayerInterface, Record> scores = this.getScores();
        List<PlayerRecordPair> pairs = new ArrayList<>();

        for(PlayerInterface p : players) pairs.add(new PlayerRecordPair(p, scores.get(p)));
        return pairs;
    }

    /**
     * OTHER FUNCTIONS
     */
    @Override
    public String results(){
        List<PlayerRecordPair> pairsSortedByWins = this.getPairs().stream()
                .sorted(Comparator.comparingInt(PlayerRecordPair::getWins))
                //.sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        JSONArray results = new JSONArray();
        for(PlayerRecordPair pair : pairsSortedByWins){
            if(!pair.getPlayer().hasCheatedWithName()) results.add(pair.toJSON());
        }
        return results.toJSONString();
    }


    @Override
    public void updateSchedule(){
    }
}
