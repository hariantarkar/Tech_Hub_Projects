import java.util.*;

public class UniquePairs {

    public static void main(String[] args) {
        int[] arr = {1, 5, 7, -1, 5, 2, 4, 3};
        int target = 6;

        Set<String> printed = new HashSet<>();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {

                if (arr[i] + arr[j] == target) {
                    int small = Math.min(arr[i], arr[j]);
                    int big = Math.max(arr[i], arr[j]);
                    String key = small + "," + big;

                   
                    if (!printed.contains(key)) {
                        printed.add(key);
                        result.add("(" + arr[i] + ", " + arr[j] + ")");
                    }
                }
            }
        }

        System.out.println("Output: " + String.join(", ", result));
    }
}