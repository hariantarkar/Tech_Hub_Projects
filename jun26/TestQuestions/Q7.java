/*Write a java program to check Number Is happy Number or Not happy using function recursion */

import java.util.*;

public class Q7 {
    public static void main(String []x){

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = sc.nextInt();
        boolean b=isHappy(num,new HashSet<Integer>());
       if (b) {
            System.out.println(num + " is a happy number.");
        } else {
            System.out.println(num + " is not a happy number.");
        }
    }
 static boolean isHappy(int n, Set<Integer> seen) {
        if (n == 1)
           return true;

         if (seen.contains(n))
         return false;

        seen.add(n);

        return isHappy(sumOfSquares(n), seen);
        }
        public static int sumOfSquares(int n) {
        int sum = 0;
        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }
        return sum;
        }
    
}


