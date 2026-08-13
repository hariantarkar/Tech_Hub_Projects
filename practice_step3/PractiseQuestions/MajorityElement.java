import java.text.NumberFormat.Style;
import java.util.HashMap;

public class MajorityElement {
    public static void main(String x[]){
        int a[]={12,1,3,1,12,56,12,44,12,12,12};

        HashMap<Integer,Integer> map=new HashMap<>();
        for(int num:a){
            if(!map.containsKey(num)){
                map.put(num, 1);
            }
            else{
                map.put(num,map.get(num)+1 );
            }

        }
        for(int key:map.keySet()){
            if(map.get(key)>a.length/2){
                 System.out.println(key );

            }
        }
     
    }
}
