package edu.northwestern.threeninethree.backgammon;
import org.json.simple.parser.*;
import org.json.simple.*;
import java.util.*;

public class TestDriver2{

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        JSONParser parser = new JSONParser();
        List<JSONObject> parsedJSON = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        /*
            Begin by taking the System.in via a scanner and accumulating it, by line, as a single string:
         */
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine());
        }

        /**
         * Next, each valid JSON Object represented as String form from the input must be parsed. This is done by
         * searching for the first, open curly-bracket: then, the state of nested-ness is tracked until an equal
         * number of open and closed curly-brackets have been found. Once this is done, a valid JSON Object has been
         * found in its String form, and must be parsed as an actual JSON Object and added to a list of all valid
         * JSONObjects.
         */
        String input = stringBuilder.toString();
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) != '{') continue;

            int first, last;
            int nestLevel = 1;

            first = i;
            while(nestLevel > 0){
                i++;
                if(input.charAt(i) == '{') ++nestLevel;
                if(input.charAt(i) == '}') --nestLevel;
            }
            last = i;

            try{
                JSONObject o = (JSONObject) parser.parse(input.substring(first, last+1));
                parsedJSON.add(o);
            }
            catch (Exception ignored) {}
        }

        /**
         * Once every valid JSONObject is accounted for, they must be grouped into arrays of 10 objects at a time, only
         * adding an object if it is additionally a "special" JSONObject, as is dictated by the 2.2 assignment
         * documentation.
         */
        int i = 0;
        JSONArray jsonArr = new JSONArray();
        while(!parsedJSON.isEmpty()){
            JSONObject nextObj = parsedJSON.remove(0);
            if(isSpecial(nextObj)){
                jsonArr.add(nextObj);
                if(++i == 10){
                    Backend.sort(jsonArr);
                }
            }
        }

        /**
         * Finally, with every JSONArray of 10 valid, special JSONObjects contained in a list, perform a series of
         * Stream/Collect operations to get the proper String representation of an Array of JSONArrays.
         */
        System.out.print(jsonArr.toJSONString());
    }

    public static boolean isSpecial(JSONObject o){
        return o.keySet().size() == 1
                && o.containsKey("content")
                && (o.get("content") instanceof Integer || o.get("content") instanceof Long)
                && 1 <= (Long)(o.get("content"))
                && (Long)(o.get("content")) <= 24;
    }
}