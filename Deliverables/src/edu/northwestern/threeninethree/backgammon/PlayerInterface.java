package edu.northwestern.threeninethree.backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface PlayerInterface {
    String name() throws CheatingException;

    void startGame(Color c, String oppName) throws CheatingException;

    Turn turn(BoardInterface b, List<Integer> dice) throws CheatingException;

    void endGame(BoardInterface b, boolean hasWon) throws CheatingException;

    Color color();

    boolean isGameActive();

    void setGameState(boolean b);

    UUID getId();

    boolean hasCheated();

    boolean hasCheatedWithName();
}
