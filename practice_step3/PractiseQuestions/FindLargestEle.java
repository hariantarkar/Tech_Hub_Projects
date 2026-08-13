import java.util.*;
public class FindLargestEle {
    public static void main (String x[]){
        int a[]={12,12,89,3,4,5,6,89};
       List<Integer> list = Arrays.asList(10, 30, 50, 20, 40);

       Arrays.sort(a);
       int largest =Collections.max(list);
        System.out.println(largest);
        
        System.out.println(a[a.length-1]);
    }
}


