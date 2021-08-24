package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONObject;

import java.util.List;

public interface BoardInterface{

    /**
     * Moves a checker of the given color from one specified position to another and updates the state of the board to reflect this.
     *
     *  @param  c  a Color object representing the given color of checker to be moved
     * @param  initPos  a Position object representing the board position a checker is being moved from
     * @param  destPos a Position object representing the board position a checker is being moved to
     */
    void move(Color c, Position initPos, Position destPos);

    /**
     * Gives the number of checkers of a given color at the specified position.
     *
     * @param  c  a String representing the given color of checker to be queried
     * @param  p an Object of instance int or String representing the position to query a checker from
     * @return      the number of checkers of the given color at the specified position
     */
    int query(Color c, Position p);

    /**
     * Returns a JSONObject containing the number of black and white checkers at each board position
     *
     * @return a JSONObject containing the number of black and white checkers at each board position
     */
    JSONObject toJSON();

    /**
     * Gives a deep copy of the state of the given Board Object
     *
     * @return a copy of this Board object
     */
    Board copy();

    /**
     * Checks whether or not a given position has one or more checkers of one color present.
     *
     * @param c the color of checkers to check for
     * @param initPos the position at which to check checkers for
     * @return a boolean indicating whether or not the given position has checkers of one color present
     */
    boolean posHasCheckers(Color c, Position initPos);

    /**
     * Checks whether or not a position is "made" for a given color, according to the rules of the game of Backgammon. A position is made if, for the given color, the opposite color has two or more checkers present at said position.
     *
     * @param c the color for which to check for a made position
     * @param destPos the position to check for being made
     * @return a boolean indicating whether or not a position is made for the given color
     */
    boolean posIsMade(Color c, Position destPos);

    /**
     * Checks whether or not a given color's bar position has one or more checkers present.
     *
     * @param c the color of bar for which to check
     * @return a boolean indicating whether or not the given color's bar has any checkers present
     */
    boolean barHasCheckers(Color c);

    /**
     * Returns a List<Integer> object, representing the integer-value of positions included in the given color's Home Quadrant.
     *
     * @param c the color of Home Quadrant positions to return
     * @return a List<Integer> object corresponding to the given color's Home Quadrant positions.
     */
    List<Integer> getHomeQuadrant(Color c);

    /**
     * Checks whether or not all checkers of a given color are within the corresponding Home Quadrant.
     *
     * @param c the color of checkers to check within their corresponding Home Quadrant.
     * @return a boolean indicating whether or not all checkers of a given color are within the corresponding Home Quadrant.
     */
    boolean colorAtHomeBoard(Color c);

    /**
     * Returns a List<Position> object containing every distinct board position.
     *
     * @return a list of unique Positions
     */
    List<Position> getAllCPOS();

    /**
     * Checks whether or not a given color has all checkers at the corresponding home position.
     *
     * @param c the color of checkers for which to check
     * @return a boolean indicating whether or not a given color has all of its checkers at home
     */
    boolean hasAllCheckersHome(Color c);

    /**
     * Determines whether or not either color has all checkers at their corresponding home position.
     *
     * @return a boolean indicating whether or not either color has all of its checkers at home
     */
    boolean hasWinner();

    /**
     * Performs a turn using the moves encapsulated in the given Turn object
     *
     * @param t a Turn object representing a turn to perform with the given board state
     */
    void performTurn(Turn t);

    int homeDistBoardScore(Color color);

    double barNumBoardScore(Color color);

}