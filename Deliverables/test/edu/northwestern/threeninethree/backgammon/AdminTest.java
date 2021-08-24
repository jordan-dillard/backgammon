package edu.northwestern.threeninethree.backgammon;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminTest {

    private final String host = "localhost";
    private final int port = 8800;
    private Admin admin;

    @Test
    public void runGame_WithOneRandomLocalPlayerLouAndOneRandomRemotePlayerPizza_ReturnsWinnerAsLouOrPizza(){
        String localPlayerStrategy = "Rando";
        String remotePlayerName = "Pizza";
        PlayerStrategy remotePlayerStrategy = PlayerStrategy.RANDOM;
        CheatConfig cheatConfig = new CheatConfig(false, false);

        setUpOneLocalOneRemote(localPlayerStrategy, remotePlayerName, remotePlayerStrategy, cheatConfig);

        List<String> expectedWinnerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        GameResult result = this.admin.runGame();
        Assert.assertTrue(expectedWinnerNames.contains(result.winnerToJSON().get("winner-name")));
    }

    @Test
    public void runGame_WithOneRandomLocalPlayerLouAndOneRandomCheatingRemotePlayerPizza_ReturnsWinnerAsLouOrReplacementPlayerMalnati(){
        String localPlayerStrategy = "Rando";
        String remotePlayerName = "Pizza";
        PlayerStrategy remotePlayerStrategy = PlayerStrategy.RANDOM;
        CheatConfig cheatConfig = new CheatConfig(true, false);

        setUpOneLocalOneRemote(localPlayerStrategy, remotePlayerName, remotePlayerStrategy, cheatConfig);

        List<String> expectedWinnerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        GameResult result = this.admin.runGame();

        Assert.assertTrue(expectedWinnerNames.contains(result.winnerToJSON().get("winner-name")));
    }

    @Test
    public void runGame_WithOneRandomLocalPlayerLouAndOneRandomNameCheatingRemotePlayerPizza_ReturnsWinnerAsLouOrReplacementPlayerMalnati(){
        String localPlayerStrategy = "Rando";
        String remotePlayerName = "Pizza";
        PlayerStrategy remotePlayerStrategy = PlayerStrategy.RANDOM;
        CheatConfig cheatConfig = new CheatConfig(false, true);

        setUpOneLocalOneRemote(localPlayerStrategy, remotePlayerName, remotePlayerStrategy, cheatConfig);

        List<String> expectedWinnerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        GameResult result = this.admin.runGame();
        Assert.assertTrue(expectedWinnerNames.contains(result.winnerToJSON().get("winner-name")));
    }

    @Test
    public void runGame_WithTwoRandomRemotePlayersLouAndPizza_ReturnsWinnerAsReplacementPlayerMalnatiOrPizza(){
        List<String> remotePlayerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        List<PlayerStrategy> remotePlayerStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheatConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(false, false),
                new CheatConfig(false, false)));

        setUpTwoRemote(remotePlayerNames, remotePlayerStrategies, cheatConfigs);

        List<String> expectedWinnerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        GameResult result = this.admin.runGame();
        Assert.assertTrue(expectedWinnerNames.contains(result.winnerToJSON().get("winner-name")));
    }

    @Test
    public void runGame_WithOneRandomCheatingRemotePlayerLouAndOneRandomRegularRemotePlayerPizza_ReturnsWinnerAsReplacementPlayerMalnatiOrPizza(){
        List<String> remotePlayerNames = new ArrayList<>(Arrays.asList("Lou", "Pizza"));
        List<PlayerStrategy> remotePlayerStrategies = new ArrayList<>(Arrays.asList(PlayerStrategy.RANDOM, PlayerStrategy.RANDOM));
        List<CheatConfig> cheatConfigs = new ArrayList<>(Arrays.asList(new CheatConfig(true, false),
                new CheatConfig(false, false)));

        setUpTwoRemote(remotePlayerNames, remotePlayerStrategies, cheatConfigs);

        List<String> expectedWinnerNames = new ArrayList<>(Arrays.asList("Malnati", "Pizza"));
        GameResult result = this.admin.runGame();
        Assert.assertTrue(expectedWinnerNames.contains(result.winnerToJSON().get("winner-name")));
    }

    @After
    public void tearDown(){

    }

    public void setUpOneLocalOneRemote(String localPlayerStrategy, String remotePlayerName, PlayerStrategy remotePlayerStrategy, CheatConfig cheatConfig){
        this.loadRemotePlayers(new ArrayList<>(Collections.singletonList(remotePlayerName)),
                new ArrayList<>(Collections.singletonList(remotePlayerStrategy)),
                new ArrayList<>(Collections.singletonList(cheatConfig)));

        this.admin = new Admin(this.port, true, localPlayerStrategy);
    }

    public void setUpTwoRemote( List<String> remotePlayerNames, List<PlayerStrategy> remotePlayerStrategies, List<CheatConfig> cheatConfigs){
        this.loadRemotePlayers(remotePlayerNames, remotePlayerStrategies, cheatConfigs);

        this.admin = new Admin(this.port, true);
    }

    public void loadRemotePlayers(List<String> remotePlayerNames, List<PlayerStrategy> remotePlayerStrategies, List<CheatConfig> cheatConfigs){

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
}
