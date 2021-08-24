package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class RemotePlayerAdapter {

    /**
     * INSTANCE FIELDS
     */
    private final PlayerInterface player;
    private final Socket clientSocket;
    private final boolean willCheat;
    private final boolean willCheatWithName;
    private final String host;
    private final int port;

    /**
     * CONSTRUCTORS
     */
    public RemotePlayerAdapter(String host, int port, PlayerInterface p){
        this.host = host;
        this.port = port;
        this.player = p;
        try {
            this.clientSocket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect remote player via adapter upon: " + e.toString());
        }
        this.willCheat = false;
        this.willCheatWithName = false;
    }

    public RemotePlayerAdapter(String host, int port, PlayerInterface p, boolean willCheat, boolean willCheatWithName){
        this.host = host;
        this.port = port;
        this.player = p;
        try {
            this.clientSocket = new Socket(this.host, this.port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect remote player via adapter.");
        }
        this.willCheat = willCheat;
        this.willCheatWithName = willCheatWithName;
    }

    /**
     * FUNCTIONS
     */
    public void run() throws IOException {

        JSONParser parser = new JSONParser();
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        PrintWriter clientWriter = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()), true);

        String parsableJSON;
        while((parsableJSON = clientReader.readLine()) != null){ // TODO: Consider changing this condition for final tournament
            try{
                String payload = "";

                if(parsableJSON.equals("\"name\"")){
                    JSONObject json;
                    if(this.willCheatWithName) json = this.marshallWrongName();
                    else json = this.marshallName();
                    payload = json.toJSONString();
                }
                else{

                    JSONObject input = (JSONObject) parser.parse(parsableJSON);

                    if(input.containsKey("start-game")){
                        this.respondToStartGame((JSONArray)input.get("start-game"));
                        payload = JSONValue.toJSONString("okay");
                    }
                    else if(input.containsKey("take-turn")){
                        JSONObject json;
                        if(this.willCheat) json = this.marshallWrongTurn();
                        else json = this.marshallTurn((JSONArray)input.get("take-turn"));
                        payload = json.toJSONString();
                    }
                    else if(input.containsKey("end-game")){
                        this.respondToEndGame((JSONArray)input.get("end-game"));
                        payload = JSONValue.toJSONString("okay");
                    }
                }
                clientWriter.println(payload);
            }
            catch(Exception e){
                this.clientSocket.close();
                throw new RuntimeException("Socket closed upon " + e.toString());
            }
        }
        this.clientSocket.close();
    }

    public JSONObject marshallName(){
        JSONObject json = new JSONObject();
        try {
            json.put("name", this.player.name());
        }
        catch(CheatingException ignored){
            // "this" player is always local
        }
        return json;
    }

    public JSONObject marshallWrongName(){
        JSONObject json = new JSONObject();
        json.put("name", new String[]{"hi"});
        return json;
    }

    public void respondToStartGame(JSONArray startGameInfo){
        Color c = Color.enumString((String)startGameInfo.get(0));
        String oppName = (String)startGameInfo.get(1);
        try{
            this.player.startGame(c, oppName);
        }
        catch(CheatingException ignored){
            // "this" player is always local
        }
    }

    public JSONObject marshallTurn(JSONArray takeTurnInfo){
        JSONObject initPosns = (JSONObject)takeTurnInfo.get(0);
        BoardInterface b = TestDriver3.unmarshallBoard(initPosns);
        List<Integer> dice = TestDriver4.unmarshallDice((JSONArray) takeTurnInfo.get(1));
        Turn t;
        try{
            t = this.player.turn(b, dice);
        }
        catch(CheatingException ignored){
            // "this" player is always local
            throw new RuntimeException("This branch should never be reached.");
        }
        JSONObject json = new JSONObject();
        json.put("turn", t.toJSON());
        return json;
    }

    public JSONObject marshallWrongTurn(){
        JSONObject json = new JSONObject();
        json.put("turn", 1);
        return json;
    }

    public void respondToEndGame(JSONArray endGameInfo){
        JSONObject initPosns = (JSONObject)endGameInfo.get(0);
        BoardInterface board = TestDriver3.unmarshallBoard(initPosns);
        boolean b = (boolean)endGameInfo.get(1);
        try{
            this.player.endGame(board, b);
        }
        catch(CheatingException ignored){
            // "this" player is always local
        }
    }

}
