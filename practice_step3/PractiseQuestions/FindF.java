import java.util.LinkedHashMap;
import java.util.Map;

public class FindF {
    public static void main(String[] args) {
        
        String str="bhushanb";
        LinkedHashMap<Character,Integer>map = new LinkedHashMap<>();
        
        for(char ch:str.toCharArray())
        {
            // Integer count =map.get(ch);
            // if (count==null) {
            //     count=0;
            // }
            // ++count;
            map.put(ch,map.getOrDefault(ch, 0)+1);
            
        }
        for(Map.Entry<Character,Integer>entrys:map.entrySet())
        {
            System.out.println(entrys.getKey()+"\t"+entrys.getValue());
        }
    }
}
