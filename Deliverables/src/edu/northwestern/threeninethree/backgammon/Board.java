package edu.northwestern.threeninethree.backgammon;

import org.json.simple.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board implements BoardInterface{

    /**
     * This implementation of a Backgammon "Board" uses positions whose number values are relative to the color of checkers
     * that occupy that position. Black checkers begin with a Bar value of 25, and must make their way toward the Black
     * Home at position value 0. White checkers begin with a Bar value of 0, and must make their way toward the White Home
     * at position value 25.
     */

    /**
     * CONSTANTS
     */
    public static final int MAX_CHECKERS_PER_PLAYER = 15; // the maximum number of checkers that each color can have present on a board at any given time
    public static final int BLACK_HOME = 0; // the integer value the black "home" Position has on the board
    public static final int WHITE_HOME = Position.MAX_NUM_VAL +1; // the integer value the white "home" Position has on the board
    public static final int BLACK_BAR = Position.MAX_NUM_VAL +1; // the integer value the black "bar" Position has on the board
    public static final int WHITE_BAR = 0; // the integer value the white "bar" Position has on the board

    /**
     * INSTANCE FIELDS
     */
    private final Map<Position, Integer> blackPosns; // the current state of each positions and their counts of black checkers
    private final Map<Position, Integer> whitePosns; // the current state of each positions and their counts of white checkers
    private final Map<Color, Map<Position, Integer>> posns; // a map containing each map to black/white checkers, with each respective color as a key
    private final List<Position> allCPOS; // a list of Position objects representing every unique board position

    /**
     * CONSTRUCTORS
     */

    public Board(){
        this(getInitBlackPosns(), getInitWhitePosns());
    }

    public Board(List<Position> initBlackPosns, List<Position> initWhitePosns){
        this.blackPosns = new HashMap<>();
        this.whitePosns = new HashMap<>();

        // a list of integer values representing each unique board position's distance from home
        List<Integer> homeDistVals = IntStream.rangeClosed(Position.HOME_DIST, Position.BAR_DIST).boxed().collect(Collectors.toList());
        Collections.swap(homeDistVals, 0, homeDistVals.size()-1); // this is done so that when the board is converted to a JSON format later, each list of checkers begins and ends with those at the "bar" and "home", respectively
        this.allCPOS = homeDistVals.stream().map(Position::new).collect(Collectors.toList());

        // Initially set the number of each checker color at every board position to 0
        for(int i : homeDistVals) {
            Position p = new Position(i);
            this.blackPosns.put(p, 0);
            this.whitePosns.put(p, 0);
        }

        for(Position p: initBlackPosns) this.blackPosns.put(p, this.blackPosns.get(p) + 1);   // Increment the number of black checkers at this board position
        for(Position p: initWhitePosns) this.whitePosns.put(p, this.whitePosns.get(p) + 1);   // Increment the number of white checkers at this board position

        this.posns = new HashMap<>();
        this.posns.put(Color.BLACK, this.blackPosns);
        this.posns.put(Color.WHITE, this.whitePosns);
    }

    public Board(Map<Position, Integer> blackPosns, Map<Position, Integer> whitePosns){
        this.blackPosns = blackPosns;
        this.whitePosns = whitePosns;

        // a list of integer values representing each unique board position's distance from home
        List<Integer> homeDistVals = IntStream.rangeClosed(Position.HOME_DIST, Position.BAR_DIST).boxed().collect(Collectors.toList());
        Collections.swap(homeDistVals, 0, homeDistVals.size()-1); // this is done so that when the board is converted to a JSON format later, each list of checkers begins and ends with those at the "bar" and "home", respectively
        this.allCPOS = homeDistVals.stream().map(Position::new).collect(Collectors.toList());

        this.posns = new HashMap<>();
        this.posns.put(Color.BLACK, this.blackPosns);
        this.posns.put(Color.WHITE, this.whitePosns);
    }

    /**
     * GETTERS
     */

    /**
     * Returns a Map<Position, Integer> object, representing the number of checkers at every position of the given board state.
     *
     * @param c the color of positions to return
     * @return the Map corresponding to the counts at every position of one color's checkers
     */
    private Map<Position, Integer> getPosns (Color c){
        return this.posns.get(c);
    }

    /**
     * Returns a List<Position> object containing every distinct board position.
     *
     * @return a list of unique Positions
     */
    public List<Position> getAllCPOS(){
        return this.allCPOS;
    }

    /**
     * Returns an ArrayList<Position> object of checker positions, used to instantiate an initial board according to the rules of the game of Backgammon. Each position represents a single Black checker present at that distinct, integer-valued Position
     *
     * @return an ArrayList<Position> object to be used with the default Board constructor
     */
    public static List<Position> getInitBlackPosns(){
        List<Position> initBlackPosns = new ArrayList<>();
        for(int i = 0; i < 5; i++) initBlackPosns.add(new Position(6));
        for(int i = 0; i < 3; i++) initBlackPosns.add(new Position(8));
        for(int i = 0; i < 5; i++) initBlackPosns.add(new Position(13));
        for(int i = 0; i < 2; i++) initBlackPosns.add(new Position(24));
        return initBlackPosns;
    }

    /**
     * Returns an ArrayList<Position> object of checker positions, used to instantiate an initial board according to the rules of the game of Backgammon. Each position represents a single White checker present at that distinct, integer-valued Position.
     *
     * @return an ArrayList<Position> object to be used with the default Board constructor for initial White checker positions
     */
    public static List<Position> getInitWhitePosns(){
        List<Position> initWhitePosns = new ArrayList<>();
        for(int i = 0; i < 2; i++) initWhitePosns.add(new Position(1));
        for(int i = 0; i < 5; i++) initWhitePosns.add(new Position(12));
        for(int i = 0; i < 3; i++) initWhitePosns.add(new Position(17));
        for(int i = 0; i < 5; i++) initWhitePosns.add(new Position(19));
        return initWhitePosns;
    }

    /**
     * Returns a List<Integer> object, representing the integer-value of positions included in the given color's Home Quadrant.
     *
     * @param c the color of Home Quadrant positions to return
     * @return a List<Integer> object corresponding to the given color's Home Quadrant positions.
     */
    public List<Integer> getHomeQuadrant(Color c){
        List<Integer> quadrant;
        if(c == Color.BLACK) {
            quadrant = IntStream.rangeClosed(1, Position.MAX_NUM_VAL /4).boxed().collect(Collectors.toList());
            Collections.reverse(quadrant);
        }
        else quadrant = IntStream.rangeClosed(Position.MAX_NUM_VAL - Position.MAX_NUM_VAL /4 + 1, Position.MAX_NUM_VAL).boxed().collect(Collectors.toList());
        return quadrant;
    }

    /**
     * PREDICATES
     */

    /**
     * Checks whether or not all checkers of a given color are within the corresponding Home Quadrant.
     *
     * @param c the color of checkers to check within their corresponding Home Quadrant.
     * @return a boolean indicating whether or not all checkers of a given color are within the corresponding Home Quadrant.
     */
    public boolean colorAtHomeBoard(Color c){
        int homeQuadSum = this.query(c, new Position(PositionType.HOME));
        for(int i : this.getHomeQuadrant(c)) homeQuadSum += this.query(c, new Position(i));
        return homeQuadSum == MAX_CHECKERS_PER_PLAYER;
    }

    /**
     * Checks whether or not a position is "made" for a given color, according to the rules of the game of Backgammon. A position is made if, for the given color, the opposite color has two or more checkers present at said position.
     *
     * @param c the color for which to check for a made position
     * @param destPos the position to check for being made
     * @return a boolean indicating whether or not a position is made for the given color
     */
    public boolean posIsMade(Color c, Position destPos){
        Color other = Color.getOppositeColor(c);
        return this.query(other, destPos) >= 2;
    }

    /**
     * Checks whether or not a given position has one or more checkers of one color present.
     *
     * @param c the color of checkers to check for
     * @param initPos the position at which to check checkers for
     * @return a boolean indicating whether or not the given position has checkers of one color present
     */
    public boolean posHasCheckers(Color c, Position initPos){
        return this.query(c, initPos) >= 1;
    }

    /**
     * Checks whether or not a given color's bar position has one or more checkers present.
     *
     * @param c the color of bar for which to check
     * @return a boolean indicating whether or not the given color's bar has any checkers present
     */
    public boolean barHasCheckers(Color c){
        return this.posHasCheckers(c, new Position(PositionType.BAR));
    }

    /**
     * Checks whether or not a given color has all checkers at the corresponding home position.
     *
     * @param c the color of checkers for which to check
     * @return a boolean indicating whether or not a given color has all of its checkers at home
     */
    public boolean hasAllCheckersHome(Color c){
        return this.query(c, new Position(PositionType.HOME)) == MAX_CHECKERS_PER_PLAYER;
    }

    /**
     * Determines whether or not either color has all checkers at their corresponding home position.
     *
     * @return a boolean indicating whether or not either color has all of its checkers at home
     */
    public boolean hasWinner(){
        return this.hasAllCheckersHome(Color.BLACK) || this.hasAllCheckersHome(Color.WHITE);
    }


    /**
     * OTHER FUNCTIONS
     */

    /**
     * Moves a checker of the given color from one specified position to another and updates the state of the board to reflect this.
     * 
     * @param  c  a Color object representing the given color of checker to be moved
     * @param  initPos  a Position object representing the board position a checker is being moved from
     * @param  destPos a Position object representing the board position a checker is being moved to
     */
    public void move(Color c, Position initPos, Position destPos){
        Map<Position, Integer> playerPosns = this.getPosns(c);

        playerPosns.put(initPos, playerPosns.get(initPos)-1);
        playerPosns.put(destPos, playerPosns.get(destPos)+1);

        Color otherColor = Color.getOppositeColor(c);
        if(destPos.isNumeric() && this.query(otherColor, destPos) >= 1) this.move(otherColor, destPos, new Position(PositionType.BAR)); // bopping
    }

    /**
     * Performs a turn using the moves encapsulated in the given Turn object
     * 
     * @param t a Turn object representing a turn to perform with the given board state
     */
    public void performTurn(Turn t){
        for(Move m : t.getMoves()){
            Color c = m.getColor();
            Position pos1 = m.getInitPos();
            Position pos2 = m.getFinalPos();
            this.move(c, pos1, pos2);
        }
    }

    /**
     * Gives the number of checkers of a given color at the specified position.
     *
     * @param  c  a String representing the given color of checker to be queried
     * @param  p an Object of instance int or String representing the position to query a checker from
     * @return      the number of checkers of the given color at the specified position
     */
    public int query(Color c, Position p){
        return this.getPosns(c).get(p);
    }

    /**
     * Gives a deep copy of the state of the given Board Object.
     *
     * @return a copy of this Board object
     * @see BoardProxy#copy()
     */
    public Board copy(){
        return new Board(new HashMap<>(this.blackPosns), new HashMap<>(this.whitePosns));
    }

    /**
     * Returns a JSONObject containing the number of black and white checkers at each board position
     *
     * @return a JSONObject containing the number of black and white checkers at each board position
     */
    public JSONObject toJSON(){
        List<Object> blackCount = new ArrayList<>();
        List<Object> whiteCount = new ArrayList<>();

        for(Position p : this.allCPOS){
            for(int i = 0; i < this.blackPosns.get(p); i++) blackCount.add(p.getAsObject());
        }
        for(Position p : this.allCPOS){
            for(int i = 0; i < this.whitePosns.get(p); i++) whiteCount.add(p.getAsObject());
        }

        JSONObject jsonBoard = new JSONObject();
        jsonBoard.put("black", blackCount);
        jsonBoard.put("white", whiteCount);

        return jsonBoard;
    }

    /**
     * Calculates a score used in the BopyHappyPlayer's score function.
     *
     * @param c the color for which the given score is calculated
     * @return an integer representing the calculated "bopping" score
     */
    public int homeDistBoardScore(Color c){
        int score = 0;
        for(Position p : this.allCPOS){
            for(int i = 0; i < this.query(c, p); i++) score += p.toHomeDist(c);
        }
        return score;
    }

    public double barNumBoardScore(Color c){
        Position bar = new Position(PositionType.BAR);
        return 0.055 * this.query(Color.getOppositeColor(c), bar) - this.query(c, bar);
    }
}