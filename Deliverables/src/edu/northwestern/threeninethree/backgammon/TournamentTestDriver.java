package edu.northwestern.threeninethree.backgammon;

public class TournamentTestDriver {

    public static void main(String[] args){
        String host = "penghu.eecs.northwestern.edu";
        int port = 9876;
        PlayerInterface player = new SmartPlayer("team27");
        RemotePlayerAdapter adapter = new RemotePlayerAdapter(host, port, player);
        try {
            adapter.run();
        } catch (Exception e) {
            throw new RuntimeException("Player adapter stopped running upon: " + e.toString());
        }
    }
}
