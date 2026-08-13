/*Store N words in a HashMap. Print words that contain at least two vowels.
Input:
java code map idea list
Output:
Words with at least 2 vowels: java code idea
Description:
Store words in HashMap.
Traverse each word manually and count vowels */
import java.util.*;
public class Q5 {
    public static void main(String x[]){

        String[] words = {"java", "code", "map", "idea", "list"};

        System.out.print("Words with at least 2 vowels.... ");
        for (String word : words) {
            int vowelCount = 0;
            for (char c : word.toCharArray()) {
                if ("aeiouAEIOU".indexOf(c) != -1) {
                    vowelCount++;
                }
            }
            if (vowelCount >= 2) {
                System.out.print(word + " ");
            }
        }

        //another way to do the same using hashmap containsKey() method
          String[] words2 = {"java", "code", "map", "idea", "list","hari"};
        HashMap<String, Integer> wordMap = new HashMap<>();
        for (String word : words2) {
            int vowelCount = 0;
            for (char c : word.toCharArray()) {
                if ("aeiouAEIOU".indexOf(c) != -1) {
                    vowelCount++;
                }
            }
            wordMap.put(word, vowelCount);
        }

        System.out.print("\nWords with at least 2 vowels: ");
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            if (entry.getValue() >= 2) {
                System.out.print(entry.getKey() + " ");
            }
        }

    }
}
