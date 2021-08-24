package edu.northwestern.threeninethree.backgammon;
import org.json.simple.parser.*;
import org.json.simple.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class TestDriver3 {

    public static void main(String[] args){

        /**
         * Begin by reading JSON input directly from System.in using an InputStreamReader and JSONParser
         */
        JSONObject input = parseJSONObject();
        String endsWith;

        /**
         * Store information regarding the key of the first JSONObject for later use when determining the final output
         */
        if(input.containsKey("ends-with-board")) endsWith = "ends-with-board";
        else if(input.containsKey("ends-with-query")) endsWith = "ends-with-query";
        else{throw new RuntimeException("Invalid board key.");}

        /**
         * Store the information from the JSONObjects representing the initial state of the board and the moves/queries
         * to be performed on said board
         */
        JSONArray boardSetup = (JSONArray) input.get(endsWith);
        JSONObject initPosns = (JSONObject) boardSetup.get(0);
        BoardInterface b = unmarshallBoard(initPosns);
        List<Object> actions = ((List<Object>) boardSetup).stream().skip(1).collect(Collectors.toList());


        /**
         * If the input was a JSONObject "ending with a board", perform every move and return a JSONObject representing
         * the final, subsequent state of the board
         */
        if(endsWith.equals("ends-with-board")){
            for(Object wrappedMove : actions){
                List<Object> moveVals = (ArrayList<Object>) wrappedMove;
                Move m = new Move((String)moveVals.get(0), moveVals.get(1), moveVals.get(2));
                b.move(m.getColor(), m.getInitPos(), m.getFinalPos());
            }

            System.out.println(b.toJSON());
        }
        /**
         * Otherwise, the input was a JSONObject "ending with a query"; perform every move, the following query and
         * return an integer value representing the number of checkers of the given color at the given board position
         */
        else{
            for(int i = 0; i < actions.size()-1; i++){
                List<Object> moveVals = (ArrayList<Object>) actions.get(i);
                Move m = new Move((String)moveVals.get(0), moveVals.get(1), moveVals.get(2));
                b.move(m.getColor(), m.getInitPos(), m.getFinalPos());
            }

            List<Object> queryArgs = (ArrayList<Object>) actions.get(actions.size()-1);
            Color c = Color.enumString((String) queryArgs.get(0));
            Position p = new Position(queryArgs.get(1));
            System.out.print(b.query(c, p));
        }

    }

    public static BoardProxy unmarshallBoard(JSONObject initPosns){
        List<Object> wrappedBlackPosns = (ArrayList<Object>)initPosns.get("black");
        List<Position> initBlackPosns = unmarshallPosns(wrappedBlackPosns);

        List<Object> wrappedWhitePosns = (ArrayList<Object>)initPosns.get("white");
        List<Position> initWhitePosns = unmarshallPosns(wrappedWhitePosns);

        return new BoardProxy(initBlackPosns, initWhitePosns);
    }

    public static List<Position> unmarshallPosns(List<Object> wrappedList){
        return (ArrayList<Position>)wrappedList.stream().map(Position::new).collect(Collectors.toList());
    }

    public static JSONObject parseJSONObject(){
        /**
         * Begin by reading JSON input directly from System.in using an InputStreamReader and JSONParser
         */
        InputStreamReader reader = new InputStreamReader(System.in);
        JSONParser parser = new JSONParser();
        JSONObject input;
        try{input = (JSONObject) parser.parse(reader);}
        catch (Exception e){throw new RuntimeException("Invalid JSON input");}
        return input;
    }

}