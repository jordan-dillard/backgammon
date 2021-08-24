package edu.northwestern.threeninethree.backgammon;

public class Position{

    /**
     * In contrast to the implementation and handling of relative position values in the Board class, the Position class provides
     *  a couple of absolute position values in relation to a given position's distance from Home, where the Home position
     *  is defined to have the position value 0. These values –– 0 for Home, 25 for Bar, and 1-24 for every other position,
     *  are agnostic to any checker color, and are used for various calculations in the RuleChecker class.
     */

    /**
     * CONSTANTS
     */
    public static final int MAX_NUM_VAL = 24; // the maximum integer value a Position can have
    public static final int HOME_DIST = 0; // the distance either "home" Position is from their respective color's home
    public static final int BAR_DIST = MAX_NUM_VAL+1; // the distance either "bar" Position is from their respective color's home

    /**
     * INSTANCE FIELDS
     */
    private final PositionType type; //
    private final int value; // the integer-valued representation of the given Position; used in algebra involving Positions

    /**
     * CONSTRUCTORS
     */

    /**
     *
     * @param t
     */
    public Position(PositionType t) {
        this.type = t;
        if(this.type == PositionType.HOME) this.value = HOME_DIST;
        else if(this.type == PositionType.BAR) this.value = BAR_DIST;
        else throw new RuntimeException("Numeric positions may not be constructed using it's PositionType.");
    }

    /**
     *
     * @param i
     */
    public Position(Integer i) {
        if(HOME_DIST <= i && i <= BAR_DIST) this.value = i;
        else throw new RuntimeException("Invalid integer-valued position: " + i);

        if(i == HOME_DIST){
            this.type = PositionType.HOME;
        }
        else if(i == BAR_DIST){
            this.type = PositionType.BAR;
        }
        else{
            this.type = PositionType.NUMERIC;
        }
    }

    /**
     *
     * @param cpos
     */
    public Position(Object cpos) {
        if (cpos instanceof String && cpos.equals("home")){
            this.type = PositionType.HOME;
            this.value = HOME_DIST;
        }
        else if (cpos instanceof String && cpos.equals("bar")){
            this.type = PositionType.BAR;
            this.value = BAR_DIST;
        }
        else if(cpos instanceof Long){
            this.type = PositionType.NUMERIC;
            int castVal = ((Long)cpos).intValue();
            if(HOME_DIST < castVal && castVal <= MAX_NUM_VAL) this.value = ((Long)cpos).intValue();
            else throw new RuntimeException("Invalid integer-valued position: " + castVal);
        }
        else throw new RuntimeException("Invalid position: " + cpos);
    }

    /**
     * GETTER FUNCTIONS
     */

    /**
     *
     * @return
     */
    public int getValue(){
        return this.value;
    }

    /**
     *
     * @return
     */
    public Object getAsObject(){
        switch (this.type){
            case BAR: return "bar";
            case HOME: return "home";
            default: return this.value;
        }
    }

    /**
     * PREDICATES
     */

    /**
     *
     * @return
     */
    public boolean isNumeric(){
        return this.type == PositionType.NUMERIC;
    }

    /**
     *
     * @return
     */
    public boolean isHome() {
        return this.type == PositionType.HOME;
    }

    /**
     *
     * @return
     */
    public boolean isBar() {
        return this.type == PositionType.BAR;
    }

    /**
     * OTHER FUNCTIONS
     */

    /**
     *
     * @param  pos  a Position against which this Position's value is compared
     * @return
     */
    public int differenceFrom(Position pos){
        return Math.abs(this.value - pos.getValue());
    }

    /**
     *
     * @param  c  the Color of the Home position to compare against
     * @return
     */
    public int differenceFromHome(Color c){
        if(c == Color.BLACK) return Math.abs(this.value - Board.BLACK_HOME);
        else return Math.abs(this.value - Board.WHITE_HOME);
    }

    /**
     *
     * @param  c  the Color of the Bar position to compare against
     * @return
     */
    public int differenceFromBar(Color c){
        if(c == Color.BLACK) return Math.abs(this.value - Board.BLACK_BAR);
        else return Math.abs(this.value - Board.WHITE_BAR);
    }

    /**
     *
     * @param c
     * @return
     */
    public int toHomeDist(Color c){
        if(this.isHome()) return Position.HOME_DIST;
        else if(this.isBar()) return Position.BAR_DIST;
        else if(c == Color.BLACK) return this.getValue();
        else return Position.BAR_DIST - this.getValue();
    }

    /**
     *
     * @param c
     * @param homeDist
     * @return
     */
    public static Position homeDistToPos(Color c, int homeDist){
        if(homeDist == Position.HOME_DIST) return new Position(PositionType.HOME);
        else if(homeDist == Position.BAR_DIST) return new Position(PositionType.BAR);
        else if(c == Color.BLACK) return new Position(homeDist);
        else return new Position(Position.BAR_DIST - homeDist);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode(){
        return this.value;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Position)) return false;
        else{
            Position p = (Position)o;
            return this.value == p.getValue();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        switch (this.type){
            case BAR: return "bar";
            case HOME: return "home";
            default: return String.valueOf(this.value);
        }
    }
}
