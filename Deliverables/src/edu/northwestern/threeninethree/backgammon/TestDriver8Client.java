package edu.northwestern.threeninethree.backgammon;

import java.io.IOException;

public class TestDriver8Client {

    public static void main(String[] args){
        int port = 8106;
        String host = "localhost";

        Runnable runnable1 = () -> {
            try {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(host, port, new RandomPlayer("a"), true, false);
                rma.run();
            }
            catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        Runnable runnable2 = () -> {
            try {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(host, port, new RandomPlayer("b"), false, false);
                rma.run();
            }
            catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        Runnable runnable3 = () -> {
            try {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(host, port, new RandomPlayer("c"), true, false);
                rma.run();
            }
            catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        Runnable runnable4 = () -> {
            try {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(host, port, new RandomPlayer("d"),false, false);
                rma.run();
            }
            catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        Runnable runnable5 = () -> {
            try {
                RemotePlayerAdapter rma = new RemotePlayerAdapter(host, port, new RandomPlayer("e"), false, false);
                rma.run();
            }
            catch (IOException e) {
                throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
            }
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
        Thread thread4 = new Thread(runnable4);
        Thread thread5 = new Thread(runnable5);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

    }
}
