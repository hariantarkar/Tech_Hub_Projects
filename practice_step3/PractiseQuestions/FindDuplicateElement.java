import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class FindDuplicateElement {
    public static void main(String x[]){
        ArrayList<Integer> al=new ArrayList<Integer>(Arrays.asList(12,2,12,4,5,6,7,5));
        HashSet<Integer> hs=new HashSet<Integer>();
        LinkedHashSet<Integer> lh=new LinkedHashSet<Integer>();
        for(int num:al){
            if(!hs.add(num)){
               
                lh.add(num);
            }
            
        }
        System.out.println(lh);
    }
}
