import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MajorityElementFromList {
    public static void main(String x[]){
     
    List<Integer> al=new ArrayList<>(Arrays.asList(1,2,2,2,3,3,2,2,2,5));

    HashMap<Integer,Integer>map=new HashMap<>();

    for(int num:al){
        if(!map.containsKey(num)){
            map.put(num,1 );
        }
        else{
            map.put(num,map.get(num)+1 );
        }

    }
    for(int key:map.keySet()){
        if(map.get(key)>al.size()/2){
            System.out.println("majority element in list: "+key);

        }
    }
}
}
