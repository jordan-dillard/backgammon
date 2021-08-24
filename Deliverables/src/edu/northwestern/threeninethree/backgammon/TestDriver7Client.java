package edu.northwestern.threeninethree.backgammon;

import java.io.*;

public class TestDriver7Client {

    public static void main (String[] args) throws IOException{

        Runnable runnable = () -> {
            RemotePlayerAdapter rma = new RemotePlayerAdapter("localhost", 8800, new PlayerProxy("Jean", PlayerStrategy.BOP_HAPPY));
            try {
                rma.run();
            } catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        runnable.run();
    }
}
