import java.util.*;
public class Q8 {
    public static void main(String[] args) {
        ArrayList<Integer>al=new ArrayList<>(Arrays.asList(12,12,2,22,12,5,7));

       Set<Integer> distinctElements = new HashSet<>();
         for (int num : al) {
            distinctElements.add(num);
        }
        System.out.println("Distinct elements are: " + distinctElements);


                Set<Integer> distinctElements1 = new LinkedHashSet<>(al);
        System.out.println("Distinct elements are Using LinkedHashSet: " + distinctElements1);
    }
    
}
