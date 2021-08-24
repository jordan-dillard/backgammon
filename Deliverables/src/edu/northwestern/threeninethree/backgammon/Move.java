package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;

public class Move{

    /**
     * INSTANCE FIELDS
     */
    private final Color color;
    private final Position initPos;
    private final Position finalPos;

    /**
     * CONSTRUCTORS
     */
    public Move(){
        this.color = null;
        this.initPos = null;
        this.finalPos = null;
    }

    public Move(String color, Object cpos1, Object cpos2){
        this.color = Color.enumString(color);
        this.initPos = new Position(cpos1);
        this.finalPos = new Position(cpos2);
    }

    public Move(Color c, Position p1, Position p2){
        this.color = c;
        this.initPos = p1;
        this.finalPos = p2;
    }

    /**
     * GETTERS
     */
    public Color getColor(){
        return this.color;
    }

    public Position getInitPos(){
        return this.initPos;
    }

    public Position getFinalPos(){
        return this.finalPos;
    }

    /**
     * PREDICATES
     */
    public boolean hasCorrectDirection(){
        if(this.color == Color.BLACK) return this.finalPos.getValue() < this.initPos.getValue();
        else {
            if(this.initPos.isBar()) return this.finalPos.getValue() > Board.WHITE_BAR;
            else if(this.finalPos.isHome()) return Board.WHITE_HOME > this.initPos.getValue();
            else return this.finalPos.getValue() > this.initPos.getValue();
        }
    }

    public boolean hasValidDelta(int die){
        if(this.initPos.isBar()) return this.finalPos.differenceFromBar(this.color) == die;
        else if(this.finalPos.isHome()) return this.initPos.differenceFromHome(this.color) == die;
        return this.dist() == die;
    }

    /**
     * OTHER FUNCTIONS
     */
    public int dist(){
        return this.initPos.differenceFrom(this.finalPos);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Move)) return false;
        else{
            Move m = (Move)o;
            return this.initPos.equals(m.getInitPos()) && this.finalPos.equals(m.getFinalPos());
        }
    }

    @Override
    public String toString(){
        return "[" + this.initPos.toString() + "," + this.finalPos.toString() + "]";
    }

    public JSONArray toJSON() {
        JSONArray move = new JSONArray();
        move.add(this.initPos.getAsObject());
        move.add(this.finalPos.getAsObject());
        return move;
    }
}
