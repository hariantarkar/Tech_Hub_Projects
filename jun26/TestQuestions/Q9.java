
import java.util.*;
public class Q9 {
    public static void main(String x[]){
        
ArrayList<Integer>numbers=new ArrayList<>(Arrays.asList(1,2,2,1,2,1,4,6,6,8,9,2,5));

      LinkedHashSet<Integer>RemoveDuplicates=new LinkedHashSet<>(numbers);
        System.out.println("Unique Elements are: "+RemoveDuplicates);
    }
}
