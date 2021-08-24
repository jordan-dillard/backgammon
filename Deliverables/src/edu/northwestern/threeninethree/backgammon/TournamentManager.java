

package edu.northwestern.threeninethree.backgammon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Operationally, a tournament manager performs the following steps:
 *
 * 1. The tournament manager receives the tournament type, the number of players to expect on stdin, according to config, below.
 * 2. The tournament manager starts a networking server on the port and then prints networking-started to stdout
 * 3. Once the server is up and running, the tournament manager waits for the specified number of players to connect via TCP/IP on the given port.
 * 4. If the tournament is single elimination, the tournament manager creates some number of players to bring the total a number of players to a power of two. For example, if 5 players participate, the tournament manager creates 3 more players. These players should make random moves.
 * 5. The tournament manager runs the tournament, closes all of the network connections to players, and reports the result to stdout.
 * 6. If the tournament is a single elimination one, the winner must be reported via the se-result non-terminal, giving the name of the winning player or false if one of the filler players wins.
 * 7. If the tournament is a round-robin tournament, the records of all players, sorted by the number of games won, should be reported according to the format of the rr-result non-terminal.
 */

public class TournamentManager {

    /**
     * INSTANCE FIELDS
     */
    private final int numPlayers;
    private final List<PlayerInterface> players;
    private final ServerSocket server;
    private final TournamentInterface tournament;

    /**
     * CONSTRUCTORS
     */
    public TournamentManager(int numPlayers, int port, String tourType){
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        try {
            this.server = new ServerSocket(port, numPlayers);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create Tournament server at port " + port + " upon: " + e.toString());
        }
        TournamentType t = TournamentType.enumString(tourType);
        this.loadTourPlayers(t);
        this.tournament = new TournamentProxy(this.players, t);
    }

    /**
     * PLAYER-LOADING FUNCTIONS
     */

    public void loadTourPlayers(TournamentType t){
        this.loadTourPlayers(t, new ArrayList<>());
    }

    public void loadTourPlayers(TournamentType t, List<String> localStrategies){
        this.loadAllLocalTourPlayers(localStrategies);
        if(this.players.size() != this.numPlayers) System.out.println("\"started\"");
        this.loadAllRemoteTourPlayers();
        if(t == TournamentType.SINGLE_ELIMINATION) this.loadAllFillerTourPlayers();
    }

    public void loadAllLocalTourPlayers(List<String> localStrategies){
        for(String strategy : localStrategies) this.loadLocalTourPlayer(strategy);
    }

    public void loadLocalTourPlayer(String strategy){
        PlayerInterface p;
        if(strategy.equals("Rando")) p = new PlayerProxy("Lou", PlayerStrategy.RANDOM);
        else if(strategy.equals("Bopsy")) p = new PlayerProxy("Lou", PlayerStrategy.BOP_HAPPY);
        else throw new RuntimeException("Invalid local player strategy: " + strategy);
        this.players.add(p);
    }

    public void loadAllRemoteTourPlayers(){
        while(this.players.size() != this.numPlayers) this.loadRemoteTourPlayer();
    }

    public void loadRemoteTourPlayer(){
        Socket connection;
        try{
            connection = this.server.accept();
            this.players.add(new PlayerProxy(connection));
        } catch (IOException e) {
            throw new RuntimeException("Unable to secure connection to remote player upon: " + e.toString());
        }
    }

    public void loadAllFillerTourPlayers(){
        int nextPowerOfTwo = this.nextPowerOfTwo(this.players.size());
        while(this.players.size() != nextPowerOfTwo){
            this.loadFillerTourPlayer();
        }
    }

    public void loadFillerTourPlayer(){
        this.players.add(new PlayerProxy("Filler " + this.getSaltString(), PlayerStrategy.RANDOM));
    }

    public int nextPowerOfTwo(int n){
        int count = 0;

        // First n in the below condition is for the case where n is 0
        if (n > 0 && (n & (n - 1)) == 0) return n;

        while(n != 0) {
            n >>= 1;
            count += 1;
        }

        return 1 << count;
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /**
     * OTHER FUNCTIONS
     */

    public String run(){
        this.tournament.run();
        return this.tournament.results();
    }
}
