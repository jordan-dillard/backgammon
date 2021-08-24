package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;

public class TestDriver7 {
    public static void main(String[] args){
        JSONObject adminConfig = unmarshallAdminConfig();
        String localStrategy = (String)adminConfig.get("local");
        int port = ((Long)adminConfig.get("port")).intValue();

        Admin a = new Admin(port, true, localStrategy);
        GameResult results = a.runGame();
        JSONObject adminGameOver = results.winnerToJSON();
        System.out.println(adminGameOver.toJSONString());
    }

    public static JSONObject unmarshallAdminConfig(){
        InputStreamReader reader = new InputStreamReader(System.in);
        JSONParser parser = new JSONParser();
        JSONObject input;
        try{input = (JSONObject) parser.parse(reader);}
        catch (Exception e){throw new RuntimeException("Invalid JSON input");}
        return input;
    }
}
