import java.util.*;

public class MoreThanNByThree {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(1); list.add(1);
        list.add(2); list.add(1); list.add(2); list.add(2); list.add(2);

        int n = list.size();
        HashMap<Integer, Integer> countMap = new HashMap<>();

        for (int num : list) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int key : countMap.keySet()) {
            if (countMap.get(key) > n / 3) {
                result.add(key);
            }
        }

        Collections.sort(result);
        System.out.println("Output:");
        System.out.println(result.toString().replace("[", "").replace("]", ""));
    }
}