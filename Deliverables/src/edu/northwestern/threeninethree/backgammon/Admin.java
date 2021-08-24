package edu.northwestern.threeninethree.backgammon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Admin {

    /**
     * INSTANCE FIELDS
     */
    private final List<PlayerInterface> players;
    private final BoardInterface board;
    private final boolean continueGameAfterCheating;
    private final int MAX_PLAYERS = 2;
    private final Color PLAYER_ONE_COLOR = Color.BLACK;
    private final Color PLAYER_TWO_COLOR = Color.WHITE;

    /**
     * CONSTRUCTORS
     */

    public Admin(int port, boolean continueGameAfterCheating){
        this.players = new ArrayList<>();
        this.board = new BoardProxy();
        ServerSocket server;
        try {
            server = new ServerSocket(port, this.MAX_PLAYERS);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create Admin server at port " + port + " upon: " + e.toString());
        }
        this.continueGameAfterCheating = continueGameAfterCheating;

        System.out.println("\"started\"");
        this.loadAllRemoteAdminPlayers(server);
    }

    public Admin(int port, boolean continueGameAfterCheating, String localStrategy){
        this.players = new ArrayList<>();
        this.board = new BoardProxy();
        ServerSocket server;
        try {
            server = new ServerSocket(port, this.MAX_PLAYERS);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create Admin server at port " + port + " upon: " + e.toString());
        }
        this.continueGameAfterCheating = continueGameAfterCheating;

        this.loadLocalAdminPlayer(localStrategy);
        System.out.println("\"started\"");
        this.loadRemoteAdminPlayer(server);
    }

    public Admin(PlayerInterface p1, PlayerInterface p2, boolean continueGameAfterCheating){
        this.players = new ArrayList<>();
        this.players.add(p1);
        this.players.add(p2);
        this.board = new BoardProxy();
        this.continueGameAfterCheating = continueGameAfterCheating;
    }

    public Admin(List<PlayerInterface> players, boolean continueGameAfterCheating){
        this.players = new ArrayList<>(players);
        this.board = new BoardProxy();
        this.continueGameAfterCheating = continueGameAfterCheating;
    }

    /**
     * PLAYER-LOADING FUNCTIONS
     */

    public void loadLocalAdminPlayer(String strategy){
        PlayerInterface p;
        if(strategy.equals("Rando")) p = new PlayerProxy("Lou", PlayerStrategy.RANDOM);
        else if(strategy.equals("Bopsy")) p = new PlayerProxy("Lou", PlayerStrategy.BOP_HAPPY);
        else throw new RuntimeException("Invalid local player strategy: " + strategy);
        this.players.add(p);
    }

    public void loadAllRemoteAdminPlayers(ServerSocket server){
        while(this.players.size() != this.MAX_PLAYERS) this.loadRemoteAdminPlayer(server);
    }

    public void loadRemoteAdminPlayer(ServerSocket server){
        Socket connection;
        try{
            connection = server.accept();
            this.players.add(new PlayerProxy(connection));
        } catch (IOException e) {
            throw new RuntimeException("Unable to secure connection to remote player upon: " + e.toString());
        }
    }

    /**
     * GAME-RUNNING FUNCTIONS
     */

    public GameResult runGame(){
        return runGameHelper(this.players.get(0), this.players.get(1));
    }

    public GameResult runGameHelper(PlayerInterface p1, PlayerInterface p2){
        GameResult results;
        try {
            this.startGame(p1, p2);

            List<PlayerInterface> playerOrder = new ArrayList<>();
            List<Integer> dice = new ArrayList<>();
            this.determineFirstTurn(p1, p2, playerOrder, dice);
            PlayerInterface turnOnePlayer = playerOrder.get(0);
            PlayerInterface turnTwoPlayer = playerOrder.get(1);

            this.administerFirstTurn(turnOnePlayer, dice);

            // alternate turns
            while (!this.board.hasWinner()) {
                this.administerTurn(turnTwoPlayer);
                this.administerTurn(turnOnePlayer);
            }
            // send end game message to both players
            this.endGame(p1, p2);

            if (this.board.hasAllCheckersHome(p1.color())) results = new GameResult(p1, p2);
            else if (this.board.hasAllCheckersHome(p2.color())) results = new GameResult(p2, p1);
            else throw new RuntimeException("Neither player has all checkers home; nobody has won.");
        } catch (CheatingException e) {
            if(e.getCode() == CheatingException.ErrorCode.CHEATING_BEFORE_GAME_START) results = this.handleCheatingBeforeStartGameHelper(p1, p2, e.getCheater());
            else results = this.handleCheatingAfterStartGameHelper(p1, p2, e.getCheater());
        }
        return results;
    }

    public GameResult continueGame(PlayerInterface p1, PlayerInterface p2){
        GameResult results;
        try {
            // alternate turns
            while (!this.board.hasWinner()) {
                this.administerTurn(p2);
                this.administerTurn(p1);
            }
            // send end game message to both players
            this.endGame(p1, p2);

            if(this.board.hasAllCheckersHome(p1.color())) results = new GameResult(p1, p2);
            else if(this.board.hasAllCheckersHome(p2.color())) results = new GameResult(p2, p1);
            else throw new RuntimeException("Neither player has all checkers home; nobody has won.");
        }
        catch(CheatingException e){
            if(e.getCode() == CheatingException.ErrorCode.CHEATING_BEFORE_GAME_START) results = this.handleCheatingBeforeStartGameHelper(p1, p2, e.getCheater());
            else results = this.handleCheatingAfterStartGameHelper(p1, p2, e.getCheater());
        }
        return results;
    }

    public void startGame(PlayerInterface p1, PlayerInterface p2) throws CheatingException{
        // ask players for names
        String p1Name = p1.name();
        String p2Name = p2.name();
        p1.startGame(this.PLAYER_ONE_COLOR, p2Name);
        p2.startGame(this.PLAYER_TWO_COLOR, p1Name);
    }

    public List<Integer> rollDice(){
        Random randGen = new Random();
        ArrayList<Integer> dice = new ArrayList<>();
        int rollOne = randGen.nextInt(6)+1;
        int rollTwo = randGen.nextInt(6)+1;
        if(rollOne == rollTwo) for(int i = 0; i < 4; i++) dice.add(rollOne);
        else {
            dice.add(rollOne);
            dice.add(rollTwo);
        }
        return dice;
    }

    public void administerFirstTurn(PlayerInterface p, List<Integer> dice) throws CheatingException{
        Turn t = p.turn(this.board, dice);
        this.board.performTurn(t);
    }

    public void administerTurn(PlayerInterface p) throws CheatingException{
        List<Integer> dice = this.rollDice();
        Turn t = p.turn(this.board, dice);
        this.board.performTurn(t);
    }

    public void endGame(PlayerInterface p1, PlayerInterface p2) throws CheatingException{
        boolean p1Won = this.board.hasAllCheckersHome(p1.color());
        p1.endGame(this.board, p1Won);
        p2.endGame(this.board, !p1Won);
    }

    public void determineFirstTurn(PlayerInterface p1, PlayerInterface p2, List<PlayerInterface> playerOrder, List<Integer> dice){
        Random randGen = new Random();
        int p1Roll = randGen.nextInt(6) + 1;
        int p2Roll = randGen.nextInt(6) + 1;
        while (p1Roll == p2Roll) {
            p1Roll = randGen.nextInt(6) + 1;
            p2Roll = randGen.nextInt(6) + 1;
        }
        if (p1Roll > p2Roll) {
            playerOrder.add(p1);
            playerOrder.add(p2);
        } else {
            playerOrder.add(p2);
            playerOrder.add(p1);
        }
        dice.add(p1Roll);
        dice.add(p2Roll);
    }


    /**
     * CHEAT-HANDLING FUNCTIONS
     */

    public GameResult handleCheatingBeforeStartGameHelper(PlayerInterface p1, PlayerInterface p2, PlayerInterface cheater){

        if(cheater.getId().equals(p1.getId())) return handleCheatingBeforeStartGame(this.PLAYER_ONE_COLOR, p1, p2);
        else if(cheater.getId().equals(p2.getId())) return handleCheatingBeforeStartGame(this.PLAYER_TWO_COLOR, p2, p1);
        else throw new RuntimeException("Neither player cheated before start-game.");
    }

    public GameResult handleCheatingBeforeStartGame(Color cheaterColor, PlayerInterface cheater, PlayerInterface nonCheater){
        if(this.continueGameAfterCheating) {
            PlayerProxy newPlayer = new PlayerProxy("Malnati", PlayerStrategy.RANDOM);
            this.players.add(newPlayer);
            try {
                newPlayer.startGame(cheaterColor, "Lou");
                nonCheater.startGame(Color.getOppositeColor(cheaterColor), nonCheater.name());
            }
            catch(CheatingException ignored){
                // In this logical branch, "newPlayer" and "nonCheater" are always local players and cannot cheat again.
            }
            return this.continueGame(newPlayer, nonCheater);
        }
        else return this.endGameEarlyBeforeStartGame(cheater, nonCheater);
    }

    public GameResult handleCheatingAfterStartGameHelper(PlayerInterface p1, PlayerInterface p2, PlayerInterface cheater){

        if(cheater.getId().equals(p1.getId())) return handleCheatingAfterStartGame(p1, p2);
        else if(cheater.getId().equals(p2.getId())) return handleCheatingAfterStartGame(p2, p1);
        else throw new RuntimeException("Neither player cheated after start-game.");
    }

    public GameResult handleCheatingAfterStartGame(PlayerInterface cheater, PlayerInterface nonCheater){
        if(this.continueGameAfterCheating) {
            PlayerProxy newPlayer = new PlayerProxy("Malnati", PlayerStrategy.RANDOM);
            this.players.add(newPlayer);
            try {
                newPlayer.startGame(cheater.color(), nonCheater.name());
            }
            catch(CheatingException ignored){
                // In this logical branch, "newPlayer" is always a local player and cannot cheat again.
            }
            return this.continueGame(newPlayer, nonCheater);
        }
        else return this.endGameEarlyAfterStartGame(cheater, nonCheater);
    }

    public GameResult endGameEarlyAfterStartGame(PlayerInterface cheater, PlayerInterface nonCheater){
        try {
            nonCheater.endGame(this.board, true);
        } catch (CheatingException ignore) {
        }
        return this.endGameEarlyBeforeStartGame(cheater, nonCheater);
    }

    public GameResult endGameEarlyBeforeStartGame(PlayerInterface cheater, PlayerInterface nonCheater){
        GameResult results = new GameResult(nonCheater, cheater);
        results.addCheater(cheater);
        return results;
    }
}
