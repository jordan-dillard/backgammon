package edu.northwestern.threeninethree.backgammon;

import org.json.simple.*;
import java.util.ArrayList;
import java.util.List;

public class TestDriver5_1 {

    public static void main(String[] args){

        JSONArray input = TestDriver4.parseJSONArray();

        JSONObject boardPosns = (JSONObject) input.get(0);
        List<Object> wrappedBlackPosns = (ArrayList<Object>)boardPosns.get("black");
        List<Object> wrappedWhitePosns = (ArrayList<Object>)boardPosns.get("white");
        List<Position> initBlackPosns = TestDriver3.unmarshallPosns(wrappedBlackPosns);
        List<Position> initWhitePosns = TestDriver3.unmarshallPosns(wrappedWhitePosns);

        BoardInterface b = new BoardProxy(initBlackPosns, initWhitePosns);
        Color c = Color.enumString((String)input.get(1));
        List<Integer> dice = TestDriver4.unmarshallDice((JSONArray) input.get(2));

        PlayerInterface p = new RandomPlayer("Jordan", c);
        try{
            Turn t = p.turn(b, dice);
            System.out.print(t.toJSON());
        }
        catch(Exception ignored){

        }
    }
}
