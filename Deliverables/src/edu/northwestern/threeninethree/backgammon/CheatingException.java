package edu.northwestern.threeninethree.backgammon;

public class CheatingException extends Exception{

    private final ErrorCode code;
    private final PlayerInterface cheater;

    public CheatingException(String errorMessage, ErrorCode code, PlayerInterface cheater){
        super(errorMessage);
        this.code = code;
        this.cheater = cheater;
    }

    public ErrorCode getCode() {
        return this.code;
    }

    public PlayerInterface getCheater(){ return this.cheater;}

    public enum ErrorCode{
        CHEATING_BEFORE_GAME_START,
        CHEATING_AFTER_GAME_START
    }
}
