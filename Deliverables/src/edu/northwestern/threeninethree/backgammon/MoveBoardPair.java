package edu.northwestern.threeninethree.backgammon;

public class MoveBoardPair {

    /**
     * INSTANCE FIELDS
     */
    private final Move m;
    private final BoardInterface b;

    /**
     * CONSTRUCTORS
     */
    public MoveBoardPair(Move m, BoardInterface b){
        this.m = m;
        this.b = b;
    }

    /**
     * GETTERS
     */
    public Move getMove(){
        return this.m;
    }

    public BoardInterface getBoard(){
        return this.b;
    }
}
