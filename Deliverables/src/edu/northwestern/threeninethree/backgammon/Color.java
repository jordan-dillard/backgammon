package edu.northwestern.threeninethree.backgammon;

public enum Color {
    BLACK,
    WHITE;

    /**
     * Gives the Color object that represents the given String ("black" or "white")
     *
     * @param color a String representing the color to be enumerated
     * @return
     */
    public static Color enumString(String color){
        if(color.equals("black")) return Color.BLACK;
        else if(color.equals("white")) return Color.WHITE;
        else throw new RuntimeException("Invalid color for enumeration: " + color);
    }

    /**
     * Gives the String representation of the given Color object (Color.BLACK or Color.WHITE)
     *
     * @param c a Color object representing the color to be represented as a String
     * @return
     */
    public static String denumString(Color c){
        if(c == BLACK) return "black";
        else if(c == WHITE) return "white";
        else throw new RuntimeException("Invalid color for String: " + c);
    }

    /**
     * Given one color, return the opposing color (Color.BLACK or Color.WHITE)
     *
     * @param c a Color object representing the color to get the opposite of
     * @return
     */
    public static Color getOppositeColor(Color c){
        if(c == Color.BLACK) return Color.WHITE;
        else return Color.BLACK;
    }
}
