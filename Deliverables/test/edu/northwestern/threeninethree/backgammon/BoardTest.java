package edu.northwestern.threeninethree.backgammon;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class BoardTest {

    /**
     * Tests for generation of initial board positions
     */

    public void getInitBlackPosns_FirstFivePosns_HavePosnValueSix(List<Position> initBlackPosns){
        for(int i = 0; i <= 4; i++) Assert.assertEquals(6, initBlackPosns.get(i).getValue());

    }

    public void getInitBlackPosns_NextThreePosns_HavePosnValueEight(List<Position> initBlackPosns){
        for(int i = 5; i <= 7; i++) Assert.assertEquals(8, initBlackPosns.get(i).getValue());
    }

    public void getInitBlackPosns_NextFivePosns_HavePosnValueThirteen(List<Position> initBlackPosns){
        for(int i = 8; i <= 12; i++) Assert.assertEquals(13, initBlackPosns.get(i).getValue());
    }

    public void getInitBlackPosns_LastThreePosns_HavePosnValueTwentyFour(List<Position> initBlackPosns){
        for(int i = 13; i <= 14; i++) Assert.assertEquals(24, initBlackPosns.get(i).getValue());
    }

    @Test
    public void getInitBlackPosns_ForInitBoard_ReturnsInitBlackPosns(){
        List<Position> initBlackPosns = Board.getInitBlackPosns();
        this.getInitBlackPosns_FirstFivePosns_HavePosnValueSix(initBlackPosns);
        this.getInitBlackPosns_NextThreePosns_HavePosnValueEight(initBlackPosns);
        this.getInitBlackPosns_NextFivePosns_HavePosnValueThirteen(initBlackPosns);
        this.getInitBlackPosns_LastThreePosns_HavePosnValueTwentyFour(initBlackPosns);
    }

    public void getInitWhitePosns_FirstTwoPosns_HavePosnValueSix(List<Position> initWhitePosns){
        for(int i = 0; i <= 1; i++) Assert.assertEquals(1, initWhitePosns.get(i).getValue());

    }

    public void getInitWhitePosns_NextFivePosns_HavePosnValueEight(List<Position> initWhitePosns){
        for(int i = 2; i <= 6; i++) Assert.assertEquals(12, initWhitePosns.get(i).getValue());
    }

    public void getInitWhitePosns_NextThreePosns_HavePosnValueThirteen(List<Position> initWhitePosns){
        for(int i = 7; i <= 9; i++) Assert.assertEquals(17, initWhitePosns.get(i).getValue());
    }

    public void getInitWhitePosns_LastFivePosns_HavePosnValueTwentyFour(List<Position> initWhitePosns){
        for(int i = 10; i <= 14; i++) Assert.assertEquals(19, initWhitePosns.get(i).getValue());
    }

    @Test
    public void getInitWhitePosns_ForInitBoard_ReturnsInitWhitePosns(){
        List<Position> initWhitePosns = Board.getInitWhitePosns();
        this.getInitWhitePosns_FirstTwoPosns_HavePosnValueSix(initWhitePosns);
        this.getInitWhitePosns_NextFivePosns_HavePosnValueEight(initWhitePosns);
        this.getInitWhitePosns_NextThreePosns_HavePosnValueThirteen(initWhitePosns);
        this.getInitWhitePosns_LastFivePosns_HavePosnValueTwentyFour(initWhitePosns);

    }

    /**
     * Tests for initial number of black checkers
     */

    @Test
    public void query_ForInitBoardBlackBar_ReturnsZeroCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(PositionType.BAR);
        Color black = Color.BLACK;
        Assert.assertEquals(0, board.query(black, pos));
    }

    @Test
    public void query_ForInitBoardBlackPosnSix_ReturnsFiveCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(6);
        Color black = Color.BLACK;
        Assert.assertEquals(5, board.query(black, pos));
    }

    @Test
    public void query_ForInitBoardBlackPosnEight_ReturnsThreeCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(8);
        Color black = Color.BLACK;
        Assert.assertEquals(3, board.query(black, pos));
    }

    @Test
    public void query_ForInitBoardBlackPosnThirteen_ReturnsFiveCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(13);
        Color black = Color.BLACK;
        Assert.assertEquals(5, board.query(black, pos));
    }

    @Test
    public void query_ForInitBoardBlackPosnTwentyFour_ReturnsTwoCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(24);
        Color black = Color.BLACK;
        Assert.assertEquals(2, board.query(black, pos));
    }

    @Test
    public void query_ForInitBoardBlackHome_ReturnsZeroCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(PositionType.HOME);
        Color black = Color.BLACK;
        Assert.assertEquals(0, board.query(black, pos));
    }

    /**
     * Test for initial number of white checkers
     */

    @Test
    public void query_ForInitBoardWhiteBar_ReturnsZeroCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(PositionType.BAR);
        Color white = Color.WHITE;
        Assert.assertEquals(0, board.query(white, pos));
    }

    @Test
    public void query_ForInitBoardWhitePosnOne_ReturnsTwoCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(1);
        Color white = Color.WHITE;
        Assert.assertEquals(2, board.query(white, pos));
    }

    @Test
    public void query_ForInitBoardWhitePosnTwelve_ReturnsFiveCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(12);
        Color white = Color.WHITE;
        Assert.assertEquals(5, board.query(white, pos));
    }

    @Test
    public void query_ForInitBoardWhitePosnSeventeen_ReturnsThreeCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(17);
        Color white = Color.WHITE;
        Assert.assertEquals(3, board.query(white, pos));
    }

    @Test
    public void query_ForInitBoardPosnNineteen_ReturnsFiveCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(19);
        Color white = Color.WHITE;
        Assert.assertEquals(5, board.query(white, pos));
    }

    @Test
    public void query_ForInitBoardWhiteHome_ReturnsZeroCheckers(){
        BoardInterface board = new BoardProxy();
        Position pos = new Position(PositionType.HOME);
        Color white = Color.WHITE;
        Assert.assertEquals(0, board.query(white, pos));
    }

    /**
     * Tests for different moves with black checkers
     */

    @Test
    public void move_FromInitBoardBlackPosnSixToFive_Succeeds(){
        BoardInterface board = new BoardProxy();
        Position pos1 = new Position(6);
        Position pos2 = new Position(5);
        Color black = Color.BLACK;

        board.move(black, pos1, pos2);
        Assertions.assertAll(
                () -> Assert.assertEquals(4, board.query(black, pos1)),
                () -> Assert.assertEquals(1, board.query(black, pos2))
        );
    }

    @Test
    public void move_FromInitBoardBlackPosnSixToOne_SucceedsAndBopsWhite(){
        BoardInterface board = new BoardProxy();
        Position pos1 = new Position(6);
        Position pos2 = new Position(1);
        Position bar = new Position(PositionType.BAR);
        Color black = Color.BLACK;
        Color white = Color.WHITE;

        board.move(black, pos1, pos2);

        Assertions.assertAll(
                () -> Assert.assertEquals(4, board.query(black, pos1)),
                () -> Assert.assertEquals(1, board.query(black, pos2)),
                () -> Assert.assertEquals(1, board.query(white, pos2)),
                () -> Assert.assertEquals(1, board.query(white, bar))
        );
    }

    /**
     * Tests for different moves with white checkers
     */

    @Test
    public void move_FromInitBoardWhitePosnOneToTwo_Succeeds(){
        BoardInterface board = new BoardProxy();
        Position pos1 = new Position(1);
        Position pos2 = new Position(2);
        Color white = Color.WHITE;

        board.move(white, pos1, pos2);

        Assertions.assertAll(
                () -> Assert.assertEquals(1, board.query(white, pos1)),
                () -> Assert.assertEquals(1, board.query(white, pos2))
        );
    }

    @Test
    public void move_FromInitBoardWhitePosnOneToSix_SucceedsAndBopsBlack(){
        BoardInterface board = new BoardProxy();
        Position pos1 = new Position(1);
        Position pos2 = new Position(6);
        Position bar = new Position(PositionType.BAR);
        Color black = Color.BLACK;
        Color white = Color.WHITE;

        board.move(white, pos1, pos2);

        Assertions.assertAll(
                () -> Assert.assertEquals(1, board.query(white, pos1)),
                () -> Assert.assertEquals(1, board.query(white, pos2)),
                () -> Assert.assertEquals(4, board.query(black, pos2)),
                () -> Assert.assertEquals(1, board.query(black, bar))
        );
    }
}
