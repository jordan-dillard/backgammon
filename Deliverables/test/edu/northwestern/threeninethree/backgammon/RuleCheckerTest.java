package edu.northwestern.threeninethree.backgammon;

import edu.northwestern.threeninethree.backgammon.*;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RuleCheckerTest {

    /**
     * Regular tests
     */

    @Test
    public void posHasCheckers_ForOccupiedPosition_ReturnsTrue(){
        BoardInterface b = new BoardProxy();
        Color c = Color.BLACK;
        Position p = new Position(6);

        assert b.posHasCheckers(c, p);
    }

    @Test
    public void posHasCheckers_ForUnoccupiedPosition_ReturnsTrue(){
        BoardInterface b = new BoardProxy();
        Color c = Color.BLACK;
        Position p = new Position(7);

        assert !b.posHasCheckers(c, p);
    }

    @Test
    public void posIsMade_ForPointMadeByWhiteCheckers_ReturnsTrue(){
        BoardInterface b = new BoardProxy();
        Color c = Color.BLACK;
        Position p = new Position(1);

        assert b.posIsMade(c, p);
    }

    @Test
    public void posIsMade_ForOpenPointWithWhiteCheckers_ReturnsFalse(){
        BoardInterface b = new BoardProxy();
        Color c = Color.BLACK;
        Position p = new Position(2);

        assert !b.posIsMade(c, p);
    }

    @Test
    public void hasValidDelta_ForPositionDistEqualToDice_ReturnsTrue(){
        Color c = Color.BLACK;
        Position p1 = new Position(2);
        Position p2 = new Position(1);
        Move m = new Move(c, p1, p2);
        int d = 1;
        assert m.hasValidDelta(d);
    }

    @Test
    public void hasValidDelta_ForPositionDistNotEqualToDice_ReturnsFalse(){
        Color c = Color.BLACK;
        Position p1 = new Position(2);
        Position p2 = new Position(1);
        Move m = new Move(c, p1, p2);
        int d = 6;
        assert !m.hasValidDelta(d);
    }

    @Test
    public void hasCorrectDirection_ForBlackCheckerMovingToLowerPosition_ReturnsTrue(){
        Color c = Color.BLACK;
        Position p1 = new Position(2);
        Position p2 = new Position(1);
        Move m = new Move(c, p1, p2);
        assert m.hasCorrectDirection();
    }

    @Test
    public void hasCorrectDirection_ForBlackCheckerMovingToHigherPosition_ReturnsFalse(){
        Color c = Color.BLACK;
        Position p1 = new Position(1);
        Position p2 = new Position(2);
        Move m = new Move(c, p1, p2);
        assert !m.hasCorrectDirection();
    }

    @Test
    public void hasCorrectDirection_ForWhiteCheckerMovingToHigherPosition_ReturnsTrue(){
        Color c = Color.WHITE;
        Position p1 = new Position(1);
        Position p2 = new Position(2);
        Move m = new Move(c, p1, p2);
        assert m.hasCorrectDirection();
    }

    @Test
    public void hasCorrectDirection_ForWhiteCheckerMovingToLowerPosition_ReturnsFalse(){
        Color c = Color.WHITE;
        Position p1 = new Position(2);
        Position p2 = new Position(1);
        Move m = new Move(c, p1, p2);
        assert !m.hasCorrectDirection();
    }

    @Test
    public void hasValidPath_ForMoveFromPosnWithoutCheckers_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(7);
        Position pos2 = new Position(2);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.hasValidPath(board, black, move, die);
    }

    @Test
    public void hasValidPath_ForMoveToPosnMadeByWhiteCheckers_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(1);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.hasValidPath(board, black, move, die);
    }

    @Test
    public void hasValidPath_ForMoveWithInvalidDelta_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(2);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.hasValidPath(board, black, move, die);
    }

    @Test
    public void hasValidPath_ForMoveWithIncorrectDirection_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(8);
        Move move = new Move(black, pos1, pos2);
        int die = 2;

        assert !RuleChecker.hasValidPath(board, black, move, die);
    }

    @Test
    public void hasValidPath_ForMoveWithBlackCheckerFromPosnSixToTwoWithDieTwo_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(2);
        Move move = new Move(black, pos1, pos2);
        int die = 4;

        assert RuleChecker.hasValidPath(board, black, move, die);
    }

    /**
     * Bar related tests
     */

    @Test
    public void accountsForBar_ForMoveFromBar_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position bar = new Position(PositionType.BAR);

        assert RuleChecker.accountsForBar(board, black, bar);
    }

    @Test
    public void accountsForBar_ForMoveFromNonBarPositionWithEmptyBar_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position bar = new Position(6);

        assert RuleChecker.accountsForBar(board, black, bar);
    }

    @Test
    public void accountsForBar_ForMoveFromNonBarPositionWithOccupiedBar_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position bar = new Position(PositionType.BAR);
        Position pos = new Position(6);

        board.move(black, pos, bar);

        assert !RuleChecker.accountsForBar(board, black, pos);
    }

    /**
     * Home related tests
     */

    @Test
    public void colorAtHomeBoard_WithAnyCheckersOutsideOfHomeQuadrant_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        assert !board.colorAtHomeBoard(black)
                && !board.colorAtHomeBoard(white);
    }

    @Test
    public void colorAtHomeBoard_WithAllCheckersInsideHomeQuadrant_ReturnsTrue(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            initBlackPosns.add(new Position(1));
            initWhitePosns.add(new Position(24));
        }
        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        assert board.colorAtHomeBoard(black)
                && board.colorAtHomeBoard(white);
    }

    @Test
    public void posToHomeDist_ForBlackCheckerAtPosnSixComparedToDieSix_ReturnsTrue(){
        Position pos = new Position(6);
        Color black = Color.BLACK;
        int die = 6;
        assert pos.toHomeDist(black) == die;
    }

    @Test
    public void posToHomeDist_ForBlackCheckerAtPosnSixComparedToDieOne_ReturnsFalse(){
        Position pos = new Position(6);
        Color black = Color.BLACK;
        int die = 1;
        assert pos.toHomeDist(black) != die;
    }

    @Test
    public void posToHomeDist_ForWhiteCheckerAtPosnTwentyFourComparedToDieOne_ReturnsTrue(){
        Position pos = new Position(24);
        Color white = Color.WHITE;
        int die = 1;
        assert pos.toHomeDist(white) == die;
    }

    @Test
    public void posToHomeDist_ForWhiteCheckerAtPosnTwentyFourComparedToDieSix_ReturnsFalse(){
        Position pos = new Position(1);
        Color white = Color.WHITE;
        int die = 6;
        assert pos.toHomeDist(white) != die;
    }

    @Test
    public void homeDistException_WithDieSixForBlackCheckerAtPosnFiveAsFurthestPosnFromHomeWithBlackCheckers_ReturnsTrue(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 6;
        Position pos = new Position(5);
        assert RuleChecker.homeDistException(board, black, die, pos);
    }

    @Test
    public void homeDistException_WithDieSixForBlackCheckerAtPosnFiveAsSecondFurthestPosnFromHomeWithBlackCheckers_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(6));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 6;
        Position pos = new Position(5);
        assert !RuleChecker.homeDistException(board, black, die, pos);
    }

    @Test
    public void accountsForHome_ForPlayerNotAtHome_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(7));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 1;
        Position pos = new Position(1);
        assert !RuleChecker.accountsForHome(board, black, pos, die);
    }

    @Test
    public void accountsForHome_ForPlayerAtHomeWithValidHomeDist_ReturnsTrue(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 1;
        Position pos = new Position(1);
        assert RuleChecker.accountsForHome(board, black, pos, die);
    }

    @Test
    public void accountsForHome_ForPlayerAtHomeWithHomeDistException_ReturnsTrue(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 6;
        Position pos = new Position(5);
        assert RuleChecker.accountsForHome(board, black, pos, die);
    }

    @Test
    public void accountsForHome_ForPlayerAtHomeButWithNeitherValidHomeDistOrHomeDistException_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(6));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        int die = 6;
        Position pos = new Position(5);
        assert !RuleChecker.accountsForHome(board, black, pos, die);
    }

    @Test
    public void isLegalHomeMove_ForMoveWithBlackCheckerFromPosnSixToOne_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(6));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(1);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.isLegalHomeMove(board, black, die, move);
    }

    @Test
    public void isLegalHomeMove_ForMoveNotFromBarWithCheckersAtBar_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(PositionType.BAR));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(5);
        Position pos2 = new Position(PositionType.HOME);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.isLegalHomeMove(board, black, die, move);
    }

    @Test
    public void isLegalHomeMove_ForMoveWithoutValidPath_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(PositionType.HOME);
        Move move = new Move(black, pos1, pos2);
        int die = 6;

        assert !RuleChecker.isLegalHomeMove(board, black, die, move);
    }

    @Test
    public void isLegalHomeMove_ForMoveNotAccountingForHome_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(4);
        Position pos2 = new Position(PositionType.HOME);
        Move move = new Move(black, pos1, pos2);
        int die = 5;

        assert !RuleChecker.isLegalHomeMove(board, black, die, move);
    }

    @Test
    public void isLegalRegularMove_ForMoveToHome_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        initBlackPosns.add(new Position(1));
        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(1);
        Position pos2 = new Position(PositionType.HOME);
        Move move = new Move(black, pos1, pos2);
        int die = 1;

        assert !RuleChecker.isLegalRegularMove(board, black, die, move);
    }

    @Test
    public void isLegalRegularMove_ForMoveNotAccountingForBar_ReturnsFalse(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 2; i++) initBlackPosns.add(new Position(2));
        for(int i = 1; i <= 3; i++) initBlackPosns.add(new Position(3));
        for(int i = 1; i <= 4; i++) initBlackPosns.add(new Position(4));
        for(int i = 1; i <= 5; i++) initBlackPosns.add(new Position(5));
        initBlackPosns.add(new Position(PositionType.BAR));

        for(int i = 1; i <= 14; i++) initWhitePosns.add(new Position(PositionType.HOME));
        initWhitePosns.add(new Position(24));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        Position pos1 = new Position(5);
        Position pos2 = new Position(1);
        Move move = new Move(black, pos1, pos2);
        int die = 4;

        assert !RuleChecker.isLegalRegularMove(board, black, die, move);
    }

    @Test
    public void isValidRegularMove_ForMoveWithInvalidPath_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(10);
        Move move = new Move(black, pos1, pos2);
        int die = 4;

        assert !RuleChecker.isLegalRegularMove(board, black, die, move);
    }

    @Test
    public void isValidRegularMove_WithDieFourForMoveWithBlackCheckerFromPosnSixToTwo_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position pos1 = new Position(6);
        Position pos2 = new Position(2);
        Move move = new Move(black, pos1, pos2);
        int die = 4;

        assert RuleChecker.isLegalRegularMove(board, black, die, move);
    }

    /**
     * Enumeration tests
     */

    @Test
    public void findOneLegalMoveWithOneDie_WithDieFourForBlackCheckerAtPosnSix_ReturnsMoveWithBlackCheckerFromPosnSixToTwo(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position initPosn = new Position(6);
        Position expectedFinalPosn = new Position(2);
        int die = 4;

        Move expectedMove = new Move(black, initPosn, expectedFinalPosn);

        assert RuleChecker.findOneLegalMoveWithOneDie(board, black, die, initPosn).equals(expectedMove);
    }

    @Test
    public void findOneLegalMoveWithOneDie_WithDieFiveForBlackCheckerAtPosnSix_ReturnsNull(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        Position initPosn = new Position(6);
        int die = 5;

        assert RuleChecker.findOneLegalMoveWithOneDie(board, black, die, initPosn) == null;
    }

    @Test
    public void findAllLegalMovesWithOneDie_WithDieFiveForInitBoardBlackCheckers_ReturnsTwoSpecificMoves(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        int die = 5;

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(black, new Position(8), new Position(3)));
        expectedMoves.add(new Move(black, new Position(13), new Position(8)));

        assert RuleChecker.findAllLegalMovesWithOneDie(board, black, die).equals(expectedMoves);
    }

    @Test
    public void findAllLegalTurnsWithOrderedDice_WithOrderedDiceOneThenTwoForBlackCheckers_ReturnsTwoOrderedTurns(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 13; i++) initBlackPosns.add(new Position(PositionType.HOME));
        for(int i = 1; i <= 13; i++) initWhitePosns.add(new Position(PositionType.HOME));

        initWhitePosns.add(new Position(6));
        initWhitePosns.add(new Position(6));
        initBlackPosns.add(new Position(7));
        initBlackPosns.add(new Position(8));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        List<Integer> orderedDice = Arrays.asList(1, 2);

        Move firstPossibleMove = new Move(black, new Position(8), new Position(7));
        Move secondPossibleMove = new Move(black, new Position(7), new Position(5));
        List<Turn> expectedLegalTurnsWithOrderedDice = new ArrayList<>();
        expectedLegalTurnsWithOrderedDice.add(new Turn(Arrays.asList(firstPossibleMove, secondPossibleMove)));
        expectedLegalTurnsWithOrderedDice.add(new Turn(Collections.singletonList(firstPossibleMove)));

        List<Turn> actualLegalTurnsWithOrderedDice = new ArrayList<>();
        RuleChecker.findAllLegalTurnsWithOrderedDice(board, black, orderedDice, actualLegalTurnsWithOrderedDice);

        for(Turn expectedTurn : expectedLegalTurnsWithOrderedDice) assert actualLegalTurnsWithOrderedDice.contains(expectedTurn);
    }

    @Test
    public void findAllLegalTurnsWithOrderedDice_WithOrderedDiceTwoThenOneForBlackCheckers_ReturnsTwoOrderedTurns(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 13; i++) initBlackPosns.add(new Position(PositionType.HOME));
        for(int i = 1; i <= 13; i++) initWhitePosns.add(new Position(PositionType.HOME));

        initWhitePosns.add(new Position(6));
        initWhitePosns.add(new Position(6));
        initBlackPosns.add(new Position(8));
        initBlackPosns.add(new Position(7));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        List<Integer> orderedDice = Arrays.asList(2, 1);

        Move firstPossibleMove = new Move(black, new Position(8), new Position(7));
        Move secondPossibleMove = new Move(black, new Position(7), new Position(5));
        List<Turn> expectedLegalTurnsWithOrderedDice = new ArrayList<>();
        expectedLegalTurnsWithOrderedDice.add(new Turn(Arrays.asList(secondPossibleMove, firstPossibleMove)));
        expectedLegalTurnsWithOrderedDice.add(new Turn(Collections.singletonList(secondPossibleMove)));

        List<Turn> actualLegalTurnsWithOrderedDice = new ArrayList<>();
        RuleChecker.findAllLegalTurnsWithOrderedDice(board, black, orderedDice, actualLegalTurnsWithOrderedDice);

        for(Turn expectedTurn : expectedLegalTurnsWithOrderedDice) assert actualLegalTurnsWithOrderedDice.contains(expectedTurn);
    }

    @Test
    public void findAllLegalTurns_WithUnorderedDiceOneAndTwoForBlackCheckers_ReturnsFourTurns(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 13; i++) initBlackPosns.add(new Position(PositionType.HOME));
        for(int i = 1; i <= 13; i++) initWhitePosns.add(new Position(PositionType.HOME));

        initWhitePosns.add(new Position(6));
        initWhitePosns.add(new Position(6));
        initBlackPosns.add(new Position(8));
        initBlackPosns.add(new Position(7));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        List<Integer> orderedDice = Arrays.asList(1, 2);

        Move firstPossibleMove = new Move(black, new Position(8), new Position(7));
        Move secondPossibleMove = new Move(black, new Position(7), new Position(5));

        List<Turn> expectedLegalTurns = new ArrayList<>();
        expectedLegalTurns.add(new Turn(Arrays.asList(firstPossibleMove, secondPossibleMove)));
        expectedLegalTurns.add(new Turn(Collections.singletonList(firstPossibleMove)));
        expectedLegalTurns.add(new Turn(Arrays.asList(secondPossibleMove, firstPossibleMove)));
        expectedLegalTurns.add(new Turn(Collections.singletonList(secondPossibleMove)));

        List<Turn> actualLegalTurns =  RuleChecker.findAllLegalTurns(board, black, orderedDice);

        for(Turn expectedTurn : expectedLegalTurns) assert actualLegalTurns.contains(expectedTurn);
    }

    @Test
    public void canUseAllDie_ForListOfMaxTwoMoveTurnsWithTwoDice_ReturnsTrue(){
        Color black = Color.BLACK;
        Move move = new Move(black, new Position(6), new Position(1));

        Turn t1 = new Turn(Arrays.asList(move, move));
        Turn t2 = new Turn(Collections.singletonList(move));

        List<Turn> turns = new ArrayList<>(Arrays.asList(t1, t2));
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 2));

        assert RuleChecker.canUseAllDie(dice, turns);
    }

    @Test
    public void canUseAllDie_ForListOfMaxTwoMoveTurnsWithFourDice_ReturnsFalse(){
        Color black = Color.BLACK;
        Move move = new Move(black, new Position(6), new Position(1));

        Turn t1 = new Turn(Arrays.asList(move, move));
        Turn t2 = new Turn(Collections.singletonList(move));

        List<Turn> turns = new ArrayList<>(Arrays.asList(t1, t2));
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        assert !RuleChecker.canUseAllDie(dice, turns);
    }

    @Test
    public void removeUnnecessaryTurns_ForListOfFourTurnsContainingTwoOneMoveTurnsWithTwoDice_ReturnsTwoTurns(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();

        for(int i = 1; i <= 13; i++) initBlackPosns.add(new Position(PositionType.HOME));
        for(int i = 1; i <= 13; i++) initWhitePosns.add(new Position(PositionType.HOME));

        initWhitePosns.add(new Position(6));
        initWhitePosns.add(new Position(6));
        initBlackPosns.add(new Position(8));
        initBlackPosns.add(new Position(7));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        List<Integer> orderedDice = Arrays.asList(1, 2);

        Move firstPossibleMove = new Move(black, new Position(8), new Position(7));
        Move secondPossibleMove = new Move(black, new Position(7), new Position(5));

        List<Turn> expectedLegalTurns = new ArrayList<>();
        expectedLegalTurns.add(new Turn(Arrays.asList(firstPossibleMove, secondPossibleMove)));
        expectedLegalTurns.add(new Turn(Arrays.asList(secondPossibleMove, firstPossibleMove)));

        List<Turn> actualLegalTurns =  RuleChecker.findAllLegalTurns(board, black, orderedDice);
        RuleChecker.removeUnnecessaryTurns(orderedDice, actualLegalTurns);

        for(Turn expectedTurn : expectedLegalTurns) assert actualLegalTurns.contains(expectedTurn);
    }

    @Test
    public void isLegalTurn_ForTurnWithMovesFromPosnSixToTwoAndSixToOneWithTwoDiceOnInitBoard_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(5, 6));

        Turn attemptedTurn = new Turn(Arrays.asList(new Move(black, new Position(6), new Position(2)),
                new Move(black, new Position(6), new Position(1))));

        assert !RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForOneMoveTurnWithTwoDiceOnInitBoard_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(5, 6));

        Turn attemptedTurn = new Turn(Collections.singletonList(new Move(black, new Position(6), new Position(2))));

        assert !RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForEmptyTurnOnInitBoard_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(5, 6));

        Turn attemptedTurn = new Turn();

        assert !RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForTwoMoveTurnWithFourDiceOnInitBoard_ReturnsFalse(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1));

        List<Move> moves = new ArrayList(Arrays.asList(new Move(black, new Position(6), new Position(5)),
                new Move(black, new Position(8), new Position(7))));
        Turn attemptedTurn = new Turn(moves);

        assert !RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForEmptyTurnWithNoAvailableMovesOnBoard_ReturnsTrue(){
        List<Position> initBlackPosns = new ArrayList<>();
        List<Position> initWhitePosns = new ArrayList<>();
        for(int i = 1; i <= 15; i++) initWhitePosns.add(new Position(12));
        for(int i = 1; i <= 15; i++) initBlackPosns.add(new Position(13));

        BoardInterface board = new BoardProxy(initBlackPosns, initWhitePosns);
        Color black = Color.BLACK;
        ArrayList<Integer> dice = new ArrayList(Arrays.asList(1, 1, 1, 1));
        Turn attemptedTurn = new Turn();

        assert RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForTurnWithMovesFromPosnSixToTwoAndSixToThreeWithDiceThreeAndFourOnInitBoard_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(3, 4));

        Turn attemptedTurn = new Turn(Arrays.asList(new Move(black, new Position(6), new Position(2)),
                new Move(black, new Position(6), new Position(3))));

        assert RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }

    @Test
    public void isLegalTurn_ForTurnWithMoveFromPosnSixToFiveFourTimesWithFourOneDiceOnInitBoard_ReturnsTrue(){
        BoardInterface board = new BoardProxy();
        Color black = Color.BLACK;
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1));

        Move move = new Move(black, new Position(6), new Position(5));
        Turn attemptedTurn = new Turn(Arrays.asList(move, move, move, move));

        assert RuleChecker.isLegalTurn(board, black, dice, attemptedTurn);
    }
}
