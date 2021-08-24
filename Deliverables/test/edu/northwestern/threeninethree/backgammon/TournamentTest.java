package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentTest {

    private final String host = "localhost";
    private final int port = 8800;

    @Test
    public void roundRobinTournament_WithNonCheatingPlayersABCD_returnsFourValidRecords(){
        List<String> remotePlayerNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        List<PlayerStrategy> remotePlayerStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM, PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheatConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(false, false),
                new CheatConfig(false, false),
                new CheatConfig(false, false),
                new CheatConfig(false, false)));
        this.setUp(remotePlayerNames, remotePlayerStrategies, cheatConfigs);

        TournamentManager tm = new TournamentManager(remotePlayerNames.size(), this.port, "round robin");
        assertRoundRobinResults(remotePlayerNames, tm.run());
    }

    @Test
    public void roundRobinTournament_WithNonCheatingPlayersBDCheatingPlayersAC_returnsFourValidRecords(){
        List<String> nonCheaterNames = new ArrayList<>(Arrays.asList("b", "d"));
        List<PlayerStrategy> nonCheaterStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> nonCheaterConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(false, false),
                new CheatConfig(false, false)));
        this.setUp(nonCheaterNames, nonCheaterStrategies, nonCheaterConfigs);

        List<String> cheaterNames = new ArrayList<>(Arrays.asList("a", "c"));
        List<PlayerStrategy> cheaterStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheaterConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(true, false),
                new CheatConfig(true, false)));
        this.setUp(cheaterNames, cheaterStrategies, cheaterConfigs);

        TournamentManager tm = new TournamentManager(nonCheaterNames.size() + cheaterNames.size(), this.port, "round robin");
        assertRoundRobinResults(nonCheaterNames, cheaterNames, tm.run());
    }

    @Test
    public void singleEliminationTournament_WithNonCheatingPlayersABCD_returnsValidWinner(){
        List<String> remotePlayerNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        List<PlayerStrategy> remotePlayerStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM, PlayerStrategy.RANDOM, PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheatConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(false, false),
                new CheatConfig(false, false),
                new CheatConfig(false, false),
                new CheatConfig(false, false),
                new CheatConfig(false, false)));
        this.setUp(remotePlayerNames, remotePlayerStrategies, cheatConfigs);

        TournamentManager tm = new TournamentManager(remotePlayerNames.size(), this.port, "single elimination");
        assertSingleEliminationResults(remotePlayerNames, tm.run());
    }

    @Test
    public void singleElimination_WithNonCheatingPlayersBDCheatingPlayersAC_returnsValidWinner(){
        List<String> nonCheaterNames = new ArrayList<>(Arrays.asList("b", "d"));
        List<PlayerStrategy> nonCheaterStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> nonCheaterConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(false, false),
                new CheatConfig(false, false)));
        this.setUp(nonCheaterNames, nonCheaterStrategies, nonCheaterConfigs);

        List<String> cheaterNames = new ArrayList<>(Arrays.asList("a", "c"));
        List<PlayerStrategy> cheaterStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheaterConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(true, false),
                new CheatConfig(true, false)));
        this.setUp(cheaterNames, cheaterStrategies, cheaterConfigs);

        TournamentManager tm = new TournamentManager(nonCheaterNames.size() + cheaterNames.size(), this.port, "single elimination");
        assertSingleEliminationResults(nonCheaterNames, tm.run());
    }

    public void setUp(List<String> remotePlayerNames, List<PlayerStrategy> remotePlayerStrategies, List<CheatConfig> cheatConfigs){

        for(int i = 0; i < remotePlayerNames.size(); i++){
            String name = remotePlayerNames.get(i);
            PlayerStrategy strat = remotePlayerStrategies.get(i);
            CheatConfig cheatConfig = cheatConfigs.get(i);
            boolean willCheat = cheatConfig.getCheatSetting();
            boolean willCheatWithName = cheatConfig.getNameCheatSetting();

            Runnable runnable = () -> {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(this.host,
                        this.port,
                        new PlayerProxy(name, strat),
                        willCheat,
                        willCheatWithName);
                try {
                    rma.run();
                } catch (IOException e) {
                    throw new RuntimeException(name + "'s remote adapter stopped running.");
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public static class CheatConfig {

        private final boolean willCheat;
        private final boolean willCheatWithName;

        public CheatConfig(boolean willCheat, boolean willCheatWithName){
            this.willCheat = willCheat;
            this.willCheatWithName = willCheatWithName;
        }

        public boolean getCheatSetting(){
            return this.willCheat;
        }

        public boolean getNameCheatSetting(){
            return this.willCheatWithName;
        }
    }

    public void assertRoundRobinResults(List<String> names, String results){
        JSONParser parser = new JSONParser();
        JSONArray wrappedResults;
        try {
            wrappedResults = (JSONArray)parser.parse(results);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        List<JSONArray> records = (List<JSONArray>)wrappedResults.stream().collect(Collectors.toList());
        for(JSONArray record : records){
            String name = (String)record.get(0);
            int wins = ((Long)record.get(1)).intValue();
            int losses = ((Long)record.get(2)).intValue();
            Assertions.assertAll(
                    () -> Assert.assertTrue(names.contains(name)),
                    () -> Assert.assertTrue(wins>=0),
                    () -> Assert.assertTrue(losses>=0),
                    () -> Assert.assertEquals(names.size()-1, wins+losses));
        }
    }

    public void assertRoundRobinResults(List<String> nonCheaterNames, List<String> cheaterNames, String results){
        JSONParser parser = new JSONParser();
        JSONArray wrappedResults;
        try {
            wrappedResults = (JSONArray)parser.parse(results);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        List<JSONArray> records = (List<JSONArray>)wrappedResults.stream().collect(Collectors.toList());
        for(JSONArray record : records){
            String name = (String)record.get(0);
            int wins = ((Long)record.get(1)).intValue();
            int losses = ((Long)record.get(2)).intValue();
            if(nonCheaterNames.contains(name)){
                String winFailMessage = String.format("Non-cheating player %s's number of wins is not greater than or equal to zero: %d", name, wins);
                String lossFailMessage = String.format("Non-cheating player %s's number of losses is not greater than or equal to zero: %d", name, losses);
                String totalFailMessage = String.format("Non-cheating player %s's number of wins and losses is not equal to one less than the total number of players: %d", name, wins+losses);

                Assertions.assertAll(
                    () -> Assert.assertTrue(winFailMessage, wins>=0),
                    () -> Assert.assertTrue(lossFailMessage, losses>=0),
                    () -> Assert.assertEquals(totalFailMessage, nonCheaterNames.size()+cheaterNames.size()-1, wins+losses));
            }
            else if(cheaterNames.contains(name)){
                String winFailMessage = String.format("Cheating player %s's number of wins is not equal to zero: %d", name, wins);
                String lossFailMessage = String.format("Cheating player %s's number of losses is not equal to one less than the total number of players: %d", name, losses);

                Assertions.assertAll(
                        () -> Assert.assertEquals(winFailMessage, 0, wins),
                        () -> Assert.assertEquals(lossFailMessage, nonCheaterNames.size()+cheaterNames.size()-1, losses));
            }
            else{
                String message = String.format("Player %s is not a part of non-cheating player names: %s", name, String.join(", ", nonCheaterNames));
                Assert.fail(message);
            }
        }
    }

    public void assertSingleEliminationResults(List<String> nonCheaterNames, String results) {
        JSONParser parser = new JSONParser();
        String winnerName = "";
        try {
            winnerName = (String)parser.parse(results);
            String failMessage = winnerName + " is not a part of non-cheating player names: " + String.join(", ", nonCheaterNames);
            Assert.assertTrue(failMessage, nonCheaterNames.contains(winnerName));
        } catch(Exception e){
            try {
                Assert.assertFalse((Boolean) parser.parse(results));
            } catch (ParseException parseException) {
                throw new RuntimeException();
            }
        }
    }
}