package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class RemoteProxy implements PlayerInterface{

    /**
     * INSTANCE FIELDS
     */
    private String name;
    private Color color;
    private boolean activeGame;

    private final Socket connection;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final JSONParser parser;
    private boolean hasCheated;
    private boolean hasCheatedWithName;
    private final UUID rpid;

    /**
     * CONSTRUCTORS
     */
    public RemoteProxy(Socket s, UUID rpid){
        this.name = null;
        this.color = null;
        this.activeGame = false;
        this.connection = s;
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            this.writer = new PrintWriter((new OutputStreamWriter(this.connection.getOutputStream())), true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get connection input/output stream upon: " + e.toString());
        }

        this.parser = new JSONParser();
        this.hasCheated = false;
        this.hasCheatedWithName = false;
        this.rpid = rpid;
    }

    /**
     * GETTER FUNCTIONS
     */
    public String name() throws CheatingException{
        if(this.connection.isClosed()) return this.name;

        String message = createNameMessage();
        this.writer.println(message);
        String jsonString;
        JSONObject wrappedName;
        try {
            jsonString = this.reader.readLine();
            wrappedName = (JSONObject) this.parser.parse(jsonString);
            this.name = (String) wrappedName.get("name");
            return this.name;
        }
        catch(Exception e){
            this.handleCheating();
            this.setNameCheatState(true);
            throw new CheatingException("Invalid JSONObject: cheating detected! Throwing to Admin.", CheatingException.ErrorCode.CHEATING_BEFORE_GAME_START, this);
        }
    }

    public Color color() {
        return this.color;
    }

    public UUID getId() { return this.rpid;}

    /**
     * SETTER FUNCTIONS
     */
    public void setGameState(boolean b) {
        this.activeGame = b;
    }

    public void setCheatState(boolean b){
        this.hasCheated = b;
    }

    public void setNameCheatState(boolean b){
        this.hasCheatedWithName = b;
    }

    /**
     * PREDICATES
     */
    public boolean isGameActive() {
        return this.activeGame;
    }

    public boolean isValidGameStateConfirmation(String jsonString){
        return jsonString.equals("\"okay\"");
    }

    public boolean isValidJSONTurn(JSONObject wrappedTurn){
        return wrappedTurn.size() == 1
                && wrappedTurn.containsKey("turn")
                && wrappedTurn.get("turn") instanceof JSONArray
                && ((JSONArray) wrappedTurn.get("turn")).size() <= 4
                && this.areValidJSONMoves(wrappedTurn);
    }

    public boolean areValidJSONMoves(JSONObject wrappedTurn){
        JSONArray turn = (JSONArray) wrappedTurn.get("turn");
        for(Object wrappedMove : turn){
            JSONArray move = (JSONArray) wrappedMove;
            if(!this.isValidJSONMove(move)) return false;
        }
        return true;
    }

    public boolean isValidJSONMove(JSONArray move){
        return move.size() == 2
                && this.isValidJSONPos(move.get(0))
                && this.isValidJSONPos(move.get(1));
    }

    public boolean isValidJSONPos(Object o){
        boolean isValidJSONPos;
        try{
            new Position(o);
            isValidJSONPos = true;
        }
        catch(Exception e){
            isValidJSONPos = false;
        }
        return isValidJSONPos;
    }

    public boolean hasCheated() {
        return this.hasCheated;
    }

    public boolean hasCheatedWithName() {
        return this.hasCheatedWithName;
    }

    /**
     * OTHER FUNCTIONS
     */
    public void startGame(Color c, String oppName) throws CheatingException{
        this.color = c;

        JSONObject message = createStartGameMessage(c, oppName);
        this.writer.println(message.toJSONString());
        String jsonString;
        try{
            jsonString = this.reader.readLine();
        }
        catch(IOException e){
            throw new RuntimeException("Unable to receive start-game confirmation upon: " + e.toString());
        }
        if(!this.isValidGameStateConfirmation(jsonString)){
            this.handleCheating();
            throw new CheatingException("Invalid start-game JSONObject: cheating detected! Throwing to Admin.", CheatingException.ErrorCode.CHEATING_BEFORE_GAME_START, this);
        }
    }

    public Turn turn(BoardInterface b, List<Integer> dice) throws CheatingException {
        JSONObject message = createTakeTurnMessage(b, dice);
        this.writer.println(message.toJSONString());
        try{
            String jsonString  = this.reader.readLine();
            JSONObject wrappedTurn = (JSONObject) this.parser.parse(jsonString);

            if(!this.isValidJSONTurn(wrappedTurn)){
                this.handleCheating(); // redundant?
                throw new RuntimeException("Invalid take-turn JSONObject: cheating detected! Throwing to local catch block.");
            }
            JSONArray turn = (JSONArray) wrappedTurn.get("turn");
            String color = Color.denumString(this.color);
            Turn t = TestDriver4.unmarshallTurn(turn, color);
            if(!RuleChecker.isLegalTurn(b, this.color, dice, t)){
                this.handleCheating(); // redundant?
                throw new RuntimeException("Illegal turn: cheating detected! Throwing to local catch block.");
            }
            return TestDriver4.unmarshallTurn(turn, color);
        }
        catch(Exception e){
            this.handleCheating();
            throw new CheatingException("Cheating detected! Throwing to Admin.", CheatingException.ErrorCode.CHEATING_AFTER_GAME_START, this);
        }
    }

    public void endGame(BoardInterface b, boolean hasWon) throws CheatingException {
        JSONObject message = createEndGameMessage(b, hasWon);
        this.writer.println(message.toJSONString());
        String jsonString;
        try{
            jsonString = this.reader.readLine();
        }
        catch(IOException e){
            throw new RuntimeException("Unable to receive end-game confirmation upon: " + e.toString());
        }
        if(!this.isValidGameStateConfirmation(jsonString)){
            this.handleCheating();
            throw new CheatingException("Invalid end-game JSONObject: cheating detected! Throwing to Admin.", CheatingException.ErrorCode.CHEATING_AFTER_GAME_START, this);
        }
    }

    public static String createNameMessage(){
        return "\"name\"";
    }

    public static JSONObject createStartGameMessage(Color c, String oppName){
        JSONArray info = new JSONArray();
        info.add(Color.denumString(c));
        info.add(oppName);

        JSONObject message = new JSONObject();
        message.put("start-game", info);
        return message;
    }

    public static JSONObject createTakeTurnMessage(BoardInterface b, List<Integer> dice){
        JSONArray info = new JSONArray();
        info.add(b.toJSON());
        info.add(dice);

        JSONObject message = new JSONObject();
        message.put("take-turn", info);
        return message;
    }

    public static JSONObject createEndGameMessage(BoardInterface b, boolean hasWon){
        JSONArray info = new JSONArray();
        info.add(b.toJSON());
        info.add(hasWon);

        JSONObject message = new JSONObject();
        message.put("end-game", info);
        return message;
    }

    public void handleCheating(){
        this.setCheatState(true);
        this.close();
    }

    public void close(){
        try {
            this.connection.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close connection socket.");
        }
    }
}
