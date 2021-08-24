package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class TestDriver5_2 {

    public static void main(String[] args){

        JSONArray input = parseDice();

        JSONObject boardPosns = (JSONObject) input.get(0);
        List<Object> wrappedBlackPosns = (ArrayList<Object>)boardPosns.get("black");
        List<Object> wrappedWhitePosns = (ArrayList<Object>)boardPosns.get("white");
        List<Position> initBlackPosns = TestDriver3.unmarshallPosns(wrappedBlackPosns);
        List<Position> initWhitePosns = TestDriver3.unmarshallPosns(wrappedWhitePosns);

        //BoardProxy origState = new BoardProxy(blackPosns, whitePosns);
        BoardInterface b = new BoardProxy(initBlackPosns, initWhitePosns);
        Color c = Color.enumString((String)input.get(1));
        List<Integer> dice = TestDriver4.unmarshallDice((JSONArray) input.get(2));

        PlayerInterface p = new BopHappyPlayer("Jordan", c);
        try{
            Turn t = p.turn(b, dice);
            System.out.print(t.toJSON());
        }
        catch(Exception ignored){

        }
    }

    public static JSONArray parseDice(){
        InputStreamReader reader = new InputStreamReader(System.in);
        JSONParser parser = new JSONParser();
        JSONArray input;
        try{input = (JSONArray) parser.parse(reader);}
        catch (Exception e){throw new RuntimeException("Invalid JSON input");}
        return input;
    }
}
