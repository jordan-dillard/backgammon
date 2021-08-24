package edu.northwestern.threeninethree.backgammon;

import java.util.*;

public class RuleChecker {

    /**
     * ASSUMPTIONS:
     * 1. The number of cpos in each of the black and white portions of board is exactly 15 and that the checkers are
     * sorted; your results must also be in that form.
     *
     * 2. Each point on the board contains either white or black checkers, but not both. (Of course, course, the home
     * and the bar may contain both colors of checkers.)
     *
     * 3.The dice are legal, i.e., if there are two dice, the numbers are different and if there are four dice, the
     * numbers are the same according to backgammon doubling rule for dice.
     */

    /**
     * RULE CHECKING FUNCTIONS
     */

    public static boolean isLegalHomeMove(BoardInterface b, Color c, int d, Move m){
        Position p1 = m.getInitPos();
        Position p2 = m.getFinalPos();
        return p2.isHome() && accountsForBar(b, c, p1) && hasValidPath(b, c, m, d) && accountsForHome(b, c, p1, d);
    }

    public static boolean isLegalRegularMove(BoardInterface b, Color c, int d, Move m){
        Position p1 = m.getInitPos();
        Position p2 = m.getFinalPos();
        return !p2.isHome() && accountsForBar(b, c, p1) && hasValidPath(b, c, m, d);
    }

    public static boolean isLegalMove(BoardInterface b, Color c, int die, Move m){
        return isLegalHomeMove(b, c, die, m) || isLegalRegularMove(b, c, die, m);
    }

    public static boolean hasValidPath(BoardInterface b, Color c, Move m, int d){
        Position p1 = m.getInitPos();
        Position p2 = m.getFinalPos();
        if(p2.isHome()) return b.posHasCheckers(c, p1);
        else return b.posHasCheckers(c, p1)
                && !b.posIsMade(c, p2)
                && m.hasValidDelta(d)
                && m.hasCorrectDirection();
    }

    public static boolean accountsForBar(BoardInterface b, Color c, Position pos1){
        if(b.barHasCheckers(c)) return pos1.isBar();
        else return true;
    }

    public static boolean homeDistException(BoardInterface b, Color c, int d, Position p){
        for(int i : b.getHomeQuadrant(c)){
            if(b.query(c, new Position(i)) >= 1){
                if(c == Color.BLACK) return d >= i && p.getValue() == i; // these two lines should be a Position function
                else return d >= Board.WHITE_BAR - i && p.getValue() == i; //
            }
        }
        return false;
    }

    public static boolean accountsForHome(BoardInterface b, Color c, Position p, int d){
        boolean playerAtHome = b.colorAtHomeBoard(c);
        boolean validHomeDist = p.toHomeDist(c) == d;
        boolean homeDistException = homeDistException(b, c, d, p);

        return playerAtHome && (validHomeDist || homeDistException);
    }

    /**
     * ENUMERATION FUNCTIONS
     */

    public static Move findOneLegalMoveWithOneDie(BoardInterface b, Color c, int d, Position p1){
        int homeDist = p1.toHomeDist(c);
        int pos2Dist = homeDist - d;
        Position p2;
        if(pos2Dist < Position.HOME_DIST) p2 = new Position(PositionType.HOME);
        else p2 = Position.homeDistToPos(c, pos2Dist);
        Move m = new Move(c, p1, p2);
        if(RuleChecker.isLegalMove(b, c, d, m)) return m;
        else return null;
    }

    /**
     * TODO: FIND A WAY TO GET RID OF getAllCPOS() HERE!!!!
     */
    public static List<Move> findAllLegalMovesWithOneDie(BoardInterface b, Color c, int d){
        List<Move> nextPossibleMoves = new ArrayList<>();
        for(Position p : b.getAllCPOS()){
            Move m = findOneLegalMoveWithOneDie(b, c, d, p);
            if(m != null) nextPossibleMoves.add(m);
        }
        return nextPossibleMoves;
    }

    public static void findAllLegalTurnsWithOrderedDiceHelper(BoardInterface currB, Color c, List<Integer> currDice, List<Move> accMoves, List<Turn> accTurns){
        if(currDice.size() == 0) accTurns.add(new Turn(accMoves));
        else{
            List<Integer> newDice = new ArrayList<>(currDice);
            int d = newDice.remove(0);
            List<Move> nextPossibleMoves = findAllLegalMovesWithOneDie(currB, c, d);

            for(Move m : nextPossibleMoves){

                List<Move> newMoves = new ArrayList<>();
                for(Move accM : accMoves) newMoves.add(new Move(c, accM.getInitPos(), accM.getFinalPos()));

                BoardInterface newB = currB.copy();
                newB.move(c, m.getInitPos(), m.getFinalPos());
                newMoves.add(new Move(c, m.getInitPos(), m.getFinalPos()));
                if(newDice.size() != 0) accTurns.add(new Turn(newMoves));
                findAllLegalTurnsWithOrderedDiceHelper(newB, c, newDice, newMoves, accTurns);
            }
        }
    }

    public static void findAllLegalTurnsWithOrderedDice(BoardInterface currB, Color c, List<Integer> currDice, List<Turn> accTurns){
        findAllLegalTurnsWithOrderedDiceHelper(currB, c, currDice, new ArrayList<>(), accTurns);
    }

    public static List<Turn> findAllLegalTurns(BoardInterface b, Color c, List<Integer> dice){
        List<Turn> possibleTurns = new ArrayList<>();
        if(dice.size() == 2){
            findAllLegalTurnsWithOrderedDice(b, c, dice, possibleTurns);
            Collections.reverse(dice);
            findAllLegalTurnsWithOrderedDice(b, c, dice, possibleTurns);
        }
        else{
            findAllLegalTurnsWithOrderedDice(b, c, dice, possibleTurns);
        }
        return possibleTurns;
    }

    public static boolean canUseAllDie(List<Integer> dice, List<Turn> turns){
        int diceNum = dice.size();
        return turns.stream().anyMatch(t -> t.length() == diceNum);
    }

    public static void removeUnnecessaryTurns(List<Integer> dice, List<Turn> turns){
        int diceNum = dice.size();
        if(canUseAllDie(dice, turns)) turns.removeIf(t -> t.length() != diceNum);
        Optional<Integer> max = turns.stream().map(Turn::length).max(Integer::compare);
        if(max.isPresent()){
            int maxUsableDice = max.get();
            turns.removeIf(t->t.length()!= maxUsableDice);
        }
    }

    public static List<Turn> findAllNecessaryLegalTurns(BoardInterface b, Color c, List<Integer> dice){
        List<Turn> enumTurns = findAllLegalTurns(b, c, dice);
        removeUnnecessaryTurns(dice, enumTurns);
        return enumTurns;
    }

    /**
     * THE FINAL FUNCTION
     */

    public static boolean isLegalTurn(BoardInterface b, Color c, List<Integer> dice, Turn t){
        List<Turn> enumTurns = findAllNecessaryLegalTurns(b, c, dice);
        return (enumTurns.size() == 0 && t.length() == 0) || enumTurns.contains(t);
    }
}