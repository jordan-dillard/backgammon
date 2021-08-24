package edu.northwestern.threeninethree.backgammon;

import org.json.simple.parser.*;
import org.json.simple.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestDriver4 {

    public static void main(String[] args){

        JSONArray input = parseJSONArray();

        /**
         * Store the information from the JSONObjects representing the initial state of the board and the moves/queries
         * to be performed on said board
         */
        JSONObject boardPosns = (JSONObject) input.get(0);

        BoardProxy b = TestDriver3.unmarshallBoard(boardPosns);
        Color c= Color.enumString((String)input.get(1));
        List<Integer> dice = unmarshallDice((JSONArray) input.get(2));
        Turn t = unmarshallTurn((JSONArray) input.get(3), (String)input.get(1));

         if(RuleChecker.isLegalTurn(b.getBoard(), c, dice, t)){
             b.performTurn(t);
             System.out.print(b.toJSON());
         }
         else System.out.print(false);
    }

    public static List<Integer> unmarshallDice(JSONArray wrappedDice){
        List<Integer> dice = new ArrayList<>();
        for (Object o : wrappedDice) dice.add(((Long) o).intValue());
        return dice;
    }

    public static Turn unmarshallTurn(JSONArray wrappedTurn, String color){
        Turn t = new Turn();
        for (Object o : wrappedTurn) {
            JSONArray moveArgs = (JSONArray) o;
            t.addMove(new Move(color, moveArgs.get(0), moveArgs.get(1)));
        }
        return t;
    }

    public static JSONArray parseJSONArray(){
        /**
         * Begin by reading JSON input directly from System.in using an InputStreamReader and JSONParser
         */
        InputStreamReader reader = new InputStreamReader(System.in);
        JSONParser parser = new JSONParser();
        JSONArray input;
        try{input = (JSONArray) parser.parse(reader);}
        catch (Exception e){throw new RuntimeException("Invalid JSON input");}
        return input;
    }
}