
//Unique Elements that appear exactly once.
import java.util.*;
public class Q10 {
    public static void main(String x[]){
        
        int[] numbers = {1, 2, 2, 1, 2, 1, 4, 6, 6, 8, 9, 2, 5};

        HashMap<Integer, Integer> frequencyMap = new HashMap<>();

        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        System.out.print("Unique Elements: ");
        for (Integer uniqueNumber : frequencyMap.keySet()) {
            if (frequencyMap.get(uniqueNumber) == 1) {
                System.out.print(uniqueNumber + " ");
            }
        }
    }
}   
