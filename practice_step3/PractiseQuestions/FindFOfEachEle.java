import java.util.LinkedHashMap;
import java.util.Map;

public class FindFOfEachEle {
    public static void main(String[] args) {
        int num[]=new int[]{12,45,33,78,45,12,3,12};

        Map<Integer,Integer>map=new LinkedHashMap<>();

       // for(int value:num){
        //    map.put(value,map.getOrDefault(value,0)+1);

       // }
       for(int value=0;value<num.length;value++){
        if(map.containsKey(value)){
            map.put(num[value],map.get(num[value])+1);
        }
        else{
            map.put(num[value], 1);

        }
       }
        //System.out.println(map);
    

    String str="harish122";
    Map<Character,Integer>map2=new LinkedHashMap<>();

    for(char ch:str.toCharArray()){
        Integer count=map2.get(ch);
        if(count==null){
            count=0;
        }
        ++count;
        map2.put(ch, count);

    } 
    for(Map.Entry<Character,Integer>entrys:map2.entrySet()){
        System.out.println(entrys.getKey()+"\t"+entrys.getValue());


    }

    }    
}
