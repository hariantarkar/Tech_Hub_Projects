/*Store N integers in a HashMap. Find the sum of digits of each number and print numbers whose
digit sum is even.
Input:
123 45 222 91 84
Output:
Numbers with Even Digit Sum: 123 222 84 */

import java.util.*;
public class Q6 {
    public static void main(String []x){
        
        int[] numbers = {123, 45, 222, 91, 84};
        System.out.print("Numbers with Even Digit Sum: ");
        for (int number : numbers) {
            int sumOfDigits = 0;
            int temp = number;
            while (temp > 0) {
                sumOfDigits += temp % 10;
                temp /= 10;
            }
            if (sumOfDigits % 2 == 0) {
                System.out.print(number + " ");
            }
        }
        //another way to do the same using hashmap 
        int[] numbers2 = {123, 45, 222, 91, 84};
        HashMap<Integer, Integer> numberMap = new HashMap<>();
        for (int number : numbers2) {
            int sumOfDigits = 0;
            int temp = number;
            while (temp > 0) {
                sumOfDigits += temp % 10;
                temp /= 10;
            }
            if(sumOfDigits %2==0){
                numberMap.put(number, sumOfDigits);
            }
            
        }
        System.out.print("\nNumbers with Even Digit Sum: ");
        System.out.println(" "+numberMap.keySet());
    }

}
