package edu.northwestern.threeninethree.backgammon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BopHappyPlayer extends Player{

    /**
     * CONSTRUCTORS
     */
    public BopHappyPlayer(String tmpName, Color tmpColor) {
        super(tmpName, tmpColor);
    }

    public BopHappyPlayer(String tmpName, UUID tmpPid) {
        super(tmpName, tmpPid);
    }

    /**
     * OTHER FUNCTIONS
     */
    @Override
    public Turn turn(BoardInterface b, List<Integer> dice) {
        return this.findAnyOptimalTurn(b, this.color(), dice);
    }

    public Turn findOptimalTurnHelper(BoardInterface b, Color c, List<Integer> dice){
        return this.findOptimalTurnWithOrderedDice(b, c, dice, new ArrayList<>());
    }

    public Turn findOptimalTurnWithOrderedDice(BoardInterface currB, Color c, List<Integer> currDice, List<Move> accMoves) {
        if (currDice.size() == 0) return new Turn(accMoves);
        else {
            List<Integer> newDice = new ArrayList<>(currDice);
            int d = newDice.remove(0);
            List<Move> nextPossibleMoves = RuleChecker.findAllLegalMovesWithOneDie(currB, c, d);

            List<Move> newMoves = new ArrayList<>();
            for (Move accM : accMoves) newMoves.add(new Move(c, accM.getInitPos(), accM.getFinalPos()));

            int maxScore = Integer.MIN_VALUE;
            MoveBoardPair bestPair = null;

            for (Move m : nextPossibleMoves) {
                BoardInterface newB = currB.copy();
                newB.move(c, m.getInitPos(), m.getFinalPos());
                int currScore = this.scoreFunction(newB);
                if ((c == Color.BLACK && currScore >= maxScore) || (c == Color.WHITE && currScore > maxScore)) { // this encompasses a very specific edge case
                    maxScore = currScore;
                    bestPair = new MoveBoardPair(new Move(c, m.getInitPos(), m.getFinalPos()), newB.copy());
                }
            }


            if(bestPair != null) newMoves.add(new Move(c, bestPair.getMove().getInitPos(), bestPair.getMove().getFinalPos()));
            else return new Turn(accMoves);

            return this.findOptimalTurnWithOrderedDice(bestPair.getBoard(), c, newDice, newMoves);
        }
    }

    public int scoreFunction(BoardInterface b){
        return Math.abs(b.homeDistBoardScore(this.color()) - b.homeDistBoardScore(Color.getOppositeColor(this.color())));
    }

    public Turn findAnyOptimalTurn(BoardInterface b, Color c, List<Integer> dice){
        Turn optimalTurn;
        if(dice.size() == 2){
            Turn turnOne = this.findOptimalTurnHelper(b, c, dice);
            Collections.reverse(dice);
            Turn turnTwo = this.findOptimalTurnHelper(b, c, dice);
            if(turnOne.length() > turnTwo.length()) optimalTurn = turnOne;
            else optimalTurn = turnTwo;
        }
        else{
            optimalTurn = this.findOptimalTurnHelper(b, c, dice);
        }
        return optimalTurn;
    }
}
