package edu.northwestern.threeninethree.backgammon;
import org.json.simple.*;
import java.util.*;

public class Backend {

    private static class JSONComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            assert(o1.containsKey("content") && o2.containsKey("content"));
            Object obj1 = o1.get("content"), obj2 = o2.get("content");
            long l1 = (long) obj1, l2 = (long) obj2;
            return Long.compare(l1, l2);
        }
    }

    public static void sort(List<JSONObject> jsonObjects) {
        JSONComparator comparator = new JSONComparator();
        jsonObjects.sort(comparator);
    }
}