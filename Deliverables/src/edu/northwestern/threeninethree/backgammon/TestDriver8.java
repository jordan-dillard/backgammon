package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;

public class TestDriver8 {

    public static void main(String[] args){
        JSONObject tourConfig = unmarshallTourConfig();
        int numPlayers = ((Long)tourConfig.get("players")).intValue();
        int port = ((Long)tourConfig.get("port")).intValue();
        String tourType = (String) tourConfig.get("type");

        TournamentManager tm = new TournamentManager(numPlayers, port, tourType);
        System.out.println(tm.run());
    }

    public static JSONObject unmarshallTourConfig(){
        InputStreamReader reader = new InputStreamReader(System.in);
        JSONParser parser = new JSONParser();
        JSONObject input;
        try{input = (JSONObject) parser.parse(reader);}
        catch (Exception e){throw new RuntimeException("Invalid JSON input");}
        return input;
    }
}