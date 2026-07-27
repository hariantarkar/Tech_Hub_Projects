import java.util.Map;
import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {
         Map<Integer, String> map = new HashMap<>();
        map.put(2, "hari");
        map.put(3, "antarkar");
        map.put(2, "harish");

        System.out.println(map);

        Map<Integer, String> map1 = new HashMap<>();

        map1.put(null, "sham");
        map1.put(2, null);
        map1.put(1, null);
        System.out.println(map1);

    }
}
