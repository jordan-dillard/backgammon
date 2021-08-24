package edu.northwestern.threeninethree.backgammon;

public enum TournamentType {
    ROUND_ROBIN,
    SINGLE_ELIMINATION;

    public static TournamentType enumString(String type){
        if(type.equals("round robin")) return TournamentType.ROUND_ROBIN;
        else if(type.equals("single elimination")) return TournamentType.SINGLE_ELIMINATION;
        else throw new RuntimeException("Invalid tournament type for enumeration: " + type);
    }
}
