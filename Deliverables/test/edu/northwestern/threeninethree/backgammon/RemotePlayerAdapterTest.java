package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemotePlayerAdapterTest {

    private ServerSocket server;
    private Socket connection;
    private String host;
    private int port;
    private PlayerInterface player;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    private JSONParser parser;

    @Before
    public void setUp(){

        this.host = "localhost";
        this.port = 8800;
        this.player = new PlayerProxy("Lou", PlayerStrategy.RANDOM);
        {
            try {
                this.server = new ServerSocket(this.port);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        Runnable runnable = () -> {
            RemotePlayerAdapter rma = new RemotePlayerAdapter(this.host, this.port, this.player);
            try{
                rma.run();
            }
            catch(IOException e){
                throw new RuntimeException("RemotePlayerAdapter stopped running.");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        try {
            this.connection = server.accept();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        {
            try{
                this.serverReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                this.serverWriter = new PrintWriter(new OutputStreamWriter(this.connection.getOutputStream()), true);
            }
            catch(IOException e){
                throw new RuntimeException();
            }
        }

        this.parser = new JSONParser();
    }

    @Test
    public void nameMessage_ForRemotePlayerAdapterWithPlayerNamedLou_ReturnsStringLou(){
        this.sendNameMessage();
        Assert.assertEquals("Lou", this.recieveNameResponse());
    }

    @Test
    public void startGameMessage_ForRemotePlayerAdapterWithBlackCheckersAndOppNameMalnati_ReturnsStringOkay(){
        this.sendStartGameMessage(Color.BLACK, "Malntai");
        Assert.assertEquals("\"okay\"", this.receiveGameStateResponse());
    }

    @Test
    public void takeTurnMessage_ForRemotePlayerAdapterWithRandomPlayer_ReturnsRandomTurn(){
        BoardInterface board = new BoardProxy();
        List<Integer> dice = new ArrayList<>(Arrays.asList(1, 2));
        Color black = Color.BLACK;

        this.sendStartGameMessage(black, "Malntai");
        this.receiveGameStateResponse();

        this.sendTakeTurnMessage(board, dice);
        Assert.assertTrue(RuleChecker.isLegalTurn(board, black, dice, this.receiveTakeTurnResponse(black)));
    }

    @Test
    public void endGameMessage_ForRemotePlayerAdapterWhosePlayerWins_ReturnsStringOkay(){
        this.sendStartGameMessage(Color.BLACK, "Malntai");
        this.receiveGameStateResponse();

        this.sendEndGameMessage(new BoardProxy(), true);
        Assert.assertEquals("\"okay\"", this.receiveGameStateResponse());
    }

    @After
    public void tearDown(){
        try {
            this.connection.close();
            this.server.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    public void sendNameMessage(){
        this.serverWriter.println(RemoteProxy.createNameMessage());
    }

    public void sendStartGameMessage(Color color, String oppName){
        this.serverWriter.println(RemoteProxy.createStartGameMessage(color, oppName));
    }

    public void sendEndGameMessage(BoardInterface board, boolean hasWon){
        this.serverWriter.println(RemoteProxy.createEndGameMessage(board, hasWon));
    }

    public void sendTakeTurnMessage(BoardInterface board, List<Integer> dice){
        this.serverWriter.println(RemoteProxy.createTakeTurnMessage(board, dice));
    }

    public String recieveNameResponse(){
        String jsonString;
        JSONObject wrappedName;
        String name;
        try {
            jsonString = this.serverReader.readLine();
            wrappedName = (JSONObject) this.parser.parse(jsonString);
            name = (String) wrappedName.get("name");
            return name;
        }
        catch(Exception e){
            throw new RuntimeException("Failed to receive name response upon: " + e.toString());
        }
    }

    public String receiveGameStateResponse(){
        try {
            return this.serverReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive start-game response upon: " + e.toString());
        }
    }

    public Turn receiveTakeTurnResponse(Color color){
        String jsonString;
        JSONObject wrappedTurn;
        try{
            jsonString = this.serverReader.readLine();
            wrappedTurn = (JSONObject) this.parser.parse(jsonString);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to receive take-turn response upon: " + e.toString());
        }
        JSONArray turn = (JSONArray) wrappedTurn.get("turn");
        return TestDriver4.unmarshallTurn(turn, Color.denumString(color));
    }
}
