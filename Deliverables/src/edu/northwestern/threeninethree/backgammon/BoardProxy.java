package edu.northwestern.threeninethree.backgammon;

import org.json.simple.*;
import java.util.ArrayList;
import java.util.List;

public class BoardProxy implements BoardInterface {

    /**
     * INSTANCE FIELDS
     */
    private final BoardInterface b;

    /**
     * CONSTRUCTORS
     */

    /**
     *
     */
    public BoardProxy() {
        this.b = new Board();
    }

    /**
     *
     * @param initBlackPosns
     * @param initWhitePosns
     */
    public BoardProxy(List<Position> initBlackPosns, List<Position> initWhitePosns) {
        this.b = new Board(initBlackPosns, initWhitePosns);
    }

    /**
     * GETTERS
     */

    /**
     * Returns a List<Integer> object, representing the integer-value of positions included in the given color's Home Quadrant.
     *
     * @param c the color of Home Quadrant positions to return
     * @return a List<Integer> object corresponding to the given color's Home Quadrant positions.
     * @see RuleChecker#homeDistException(BoardInterface, Color, int, Position)
     */
    public List<Integer> getHomeQuadrant(Color c) {
        return this.b.getHomeQuadrant(c);
    }

    /**
     * PREDICATES
     */

    /**
     * Checks whether or not a given position has one or more checkers of one color present.
     *
     * @param c the color of checkers to check for
     * @param initPos the position at which to check checkers for
     * @return a boolean indicating whether or not the given position has checkers of one color present
     * @see RuleChecker#hasValidPath(BoardInterface, Color, Move, int) 
     */
    public boolean posHasCheckers(Color c, Position initPos) {
        return this.b.posHasCheckers(c, initPos);
    }

    /**
     * Checks whether or not a position is "made" for a given color, according to the rules of the game of Backgammon. A position is made if, for the given color, the opposite color has two or more checkers present at said position.
     *
     * @param c the color for which to check for a made position
     * @param destPos the position to check for being made
     * @return a boolean indicating whether or not a position is made for the given color
     * @see RuleChecker#hasValidPath(BoardInterface, Color, Move, int)
     */
    public boolean posIsMade(Color c, Position destPos) {
        return this.b.posIsMade(c, destPos);
    }

    /**
     * Checks whether or not a given color's bar position has one or more checkers present.
     *
     * @param c the color of bar for which to check
     * @return a boolean indicating whether or not the given color's bar has any checkers present
     * @see RuleChecker#accountsForBar(BoardInterface, Color, Position) 
     */
    public boolean barHasCheckers(Color c) {
        return this.b.barHasCheckers(c);
    }

    /**
     * Checks whether or not all checkers of a given color are within the corresponding Home Quadrant.
     *
     * @param c the color of checkers to check within their corresponding Home Quadrant.
     * @return a boolean indicating whether or not all checkers of a given color are within the corresponding Home Quadrant.
     * @see RuleChecker#accountsForHome(BoardInterface, Color, Position, int) 
     */
    public boolean colorAtHomeBoard(Color c) {
        return this.b.colorAtHomeBoard(c);
    }

    /**
     * Checks whether or not a given color has all checkers at the corresponding home position.
     *
     * @param c the color of checkers for which to check
     * @return a boolean indicating whether or not a given color has all of its checkers at home)
     */
    public boolean hasAllCheckersHome(Color c) {
        return this.b.hasAllCheckersHome(c);
    }

    /**
     * Determines whether or not either color has all checkers at their corresponding home position.
     *
     * @return a boolean indicating whether or not either color has all of its checkers at home
     */
    public boolean hasWinner() {
        return this.b.hasWinner();
    }

    /**
     * OTHER FUNCTIONS
     */

    /**
     * Moves a checker of the given color from one specified position to another and updates the state of the board to reflect this.
     *
     * @param  c  a String representing the given color of checker to be moved
     * @param  initPos  a Position object representing the board position a checker is being moved from
     * @param  destPos a Position object representing the board position a checker is being moved to
     * @see RuleChecker#findAllLegalTurnsWithOrderedDice(BoardInterface, Color, ArrayList, ArrayList, ArrayList) 
     * @see BopHappyPlayer#findOptimalTurnWithOrderedDice(BoardInterface, Color, ArrayList, ArrayList) 
     */
    public void move(Color c, Position initPos, Position destPos) {
        this.b.move(c, initPos, destPos);
    }

    /**
     * Gives the number of checkers of a given color at the specified position.
     *
     * @param  c  a String representing the given color of checker to be queried
     * @param  p an Object of instance int or String representing the position to query a checker from
     * @return      the number of checkers of the given color at the specified position
     * @see RuleChecker#homeDistException(BoardInterface, Color, int, Position) 
     */
    public int query(Color c, Position p) {
        return this.b.query(c, p);
    }

    /**
     * Performs a turn using the moves encapsulated in the given Turn object
     *
     * @param t a Turn object representing a turn to perform with the given board state
     */
    public void performTurn(Turn t) {
        this.b.performTurn(t);
    }

    public int homeDistBoardScore(Color color) {
        return this.b.homeDistBoardScore(color);
    }

    public double barNumBoardScore(Color color) { return this.b.barNumBoardScore(color); }

    /**
     * Gives a deep copy of the state of the given Board Object
     *
     * @return a copy of this Board object
     * @see RuleChecker#findAllLegalTurnsWithOrderedDice(BoardInterface, Color, ArrayList, ArrayList, ArrayList)
     * @see BopHappyPlayer#findOptimalTurnWithOrderedDice(BoardInterface, Color, ArrayList, ArrayList) 
     */
    public Board copy() {
        return this.b.copy();
    }

    /**
     * Returns a JSONObject containing the number of black and white checkers at each board position
     *
     * @return a JSONObject containing the number of black and white checkers at each board position
     * @see RemoteProxy#createTakeTurnMessage(BoardInterface, ArrayList)
     * @see RemoteProxy#createEndGameMessage(BoardInterface, boolean) 
     */
    public JSONObject toJSON() {
        return this.b.toJSON();
    }

    /**
     * Returns a List<Position> object containing every distinct board position.
     *
     * @return a list of unique Positions
     * @see RuleChecker#findAllLegalMovesWithOneDie(BoardInterface, Color, int) 
     */
    public List<Position> getAllCPOS() {
        return this.b.getAllCPOS();
    }

    // find a way to remove this...
    public BoardInterface getBoard() {
        return this.b;
    }
}
