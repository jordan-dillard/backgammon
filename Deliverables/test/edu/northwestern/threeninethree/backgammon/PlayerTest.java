package edu.northwestern.threeninethree.backgammon;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTest {

    @Test
    public void RandomPlayer_turnWithTwoDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(3, 4));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new RandomPlayer("Random Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }

    @Test
    public void RandomPlayer_turnWithFourDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new RandomPlayer("Random Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }

    @Test
    public void BopHappyPlayer_turnWithTwoDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(3, 4));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new BopHappyPlayer("BopHappy Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }

    @Test
    public void BopHappyPlayer_turnWithFourDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new BopHappyPlayer("BopHappy Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }

    @Test
    public void SmartPlayer_turnWithTwoDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(3, 4));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new RandomPlayer("Smart Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }

    @Test
    public void SmartPlayer_turnWithFourDice_ReturnsValidTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1));
        Color black = Color.BLACK;
        PlayerInterface randomPlayer = new RandomPlayer("Smart Player", black);
        Turn turn;
        try {
            turn = randomPlayer.turn(board, dice);
        } catch (CheatingException ignored) {
            throw new RuntimeException("This shouldn't happen");
        }
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, turn));
    }
}
