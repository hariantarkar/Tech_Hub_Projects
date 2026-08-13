/*Store N strings in a LinkedHashSet. Remove all palindrome strings.
Input:
level java radar code madam
Output:
Modified Set: java code
Description:
Check each string manually for palindrome (reverse using loop).
Remove palindrome elements using iterator */
import java.util.*;

public class Q4 {
    public static void main(String []arg){
        
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("level");
        set.add("java");
        set.add("radar");
        set.add("code");
        set.add("madam");

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            String reversedStr = "";
            for (int i = str.length() - 1; i >= 0; i--) {
                reversedStr += str.charAt(i);
            }
            if (str.equals(reversedStr)) {
                iterator.remove();
            }
        }

        System.out.println("Modified Set: " + set);

        //another way to do same using inbuilt StringBuilder reverse method
        LinkedHashSet<String> set2 = new LinkedHashSet<String>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of strings: ");
        int n = sc.nextInt();
        sc.nextLine(); // Consume the newline

        System.out.println("Enter Strings:");

        for (int i = 0; i < n; i++) {
            String str = sc.nextLine();
            set2.add(str);
        }

        Iterator<String> iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            String str = iterator2.next();
            String reversedStr = new StringBuilder(str).reverse().toString();
            if (str.equals(reversedStr)) {
                iterator2.remove();
            }
        }

        System.out.println("Modified Set (using StringBuilder): " + set2);
    }
}

