package edu.northwestern.threeninethree.backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomPlayer extends Player {

    /**
     * CONSTRUCTORS
     */
    public RandomPlayer(String tmpName, Color tmpColor) {
        super(tmpName, tmpColor);
    }

    public RandomPlayer(String tmpName, UUID tmpPid) {
        super(tmpName, tmpPid);
    }

    public RandomPlayer(String tmpName) {
        super(tmpName);
    }

    /**
     * FUNCTIONS
     */
    @Override
    public Turn turn(BoardInterface b, List<Integer> dice) {
        List<Turn> possibleTurns = RuleChecker.findAllNecessaryLegalTurns(b, this.color(), dice);
        Random randGen = new Random();
        if(possibleTurns.size() != 0) return possibleTurns.get(randGen.nextInt(possibleTurns.size()));
        else return new Turn();
    }
}
