package edu.northwestern.threeninethree.backgammon;

import org.json.simple.JSONObject;

public class TestDriver6 {

    public static void main(String[] args){
        JSONObject input = TestDriver3.parseJSONObject();
        String host = (String)input.get("host");
        int port = ((Long)input.get("port")).intValue();
        PlayerInterface player = new RandomPlayer("Jordan");
        RemotePlayerAdapter adapter = new RemotePlayerAdapter(host,port, player);
        try {
            adapter.run();
        } catch (Exception e) {
            throw new RuntimeException("RemotePlayerAdapter stopped running due to: " + e.toString());
        }
    }

}
