package edu.northwestern.threeninethree.backgammon;

import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class PlayerProxy implements PlayerInterface{

    /**
     * INSTANCE FIELDS
     */
    private final PlayerInterface player;
    private final UUID pid;

    /**
     * CONSTRUCTORS
     */

    public PlayerProxy(String name, PlayerStrategy strategy){
        this.pid = UUID.randomUUID();
        if(strategy == PlayerStrategy.RANDOM) this.player = new RandomPlayer(name, this.pid);
        else if(strategy == PlayerStrategy.BOP_HAPPY) this.player = new BopHappyPlayer(name, this.pid);
        else if(strategy == PlayerStrategy.SMART) this.player = new SmartPlayer(name, this.pid);
        else throw new RuntimeException("Invalid player strategy: " + strategy);
    }

    public PlayerProxy(Socket s){
        this.pid = UUID.randomUUID();
        this.player = new RemoteProxy(s, this.pid);
    }

    /**
     * GETTERS
     */
    public String name() throws CheatingException {
        return this.player.name();
    }

    public Color color() {
        return this.player.color();
    }

    public UUID getId(){
        return this.pid;
    }

    /**
     * SETTERS
     */
    public void setGameState(boolean b) {
        this.player.setGameState(b);
    }

    /**
     * PREDICATES
     */
    public boolean isGameActive() {
        return this.player.isGameActive();
    }

    public boolean hasCheated() {
        return this.player.hasCheated();
    }

    public boolean hasCheatedWithName() {
        return this.player.hasCheatedWithName();
    }

    /**
     * OTHER FUNCTIONS
     */
    public void startGame(Color c, String oppName) throws CheatingException {
        if(!this.player.isGameActive()){
            this.player.setGameState(true);
            this.player.startGame(c, oppName);
        }
        else throw new RuntimeException("Cannot start a new game while one is already in session.");
    }

    public Turn turn(BoardInterface b, List<Integer> dice) throws CheatingException {
        if(this.player.isGameActive()) return this.player.turn(b, dice);
        else throw new RuntimeException("Cannot take a turn without an active game.");
    }

    public void endGame(BoardInterface b, boolean hasWon) throws CheatingException {
        if(this.player.isGameActive()){
            this.player.setGameState(false);
            this.player.endGame(b, hasWon);
        }
        else throw new RuntimeException("Cannot end a game that is not active.");
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PlayerProxy)) return false;
        else{
            PlayerInterface p = (PlayerProxy) o;
            return this.pid.equals(p.getId());
        }
    }

    @Override
    public int hashCode(){
        return this.pid.toString().hashCode();
    }
}
