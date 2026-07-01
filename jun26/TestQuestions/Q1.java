/*Store multiple words and group them according to their length using HashMap<Integer,
ArrayList<String>>.
Input Example:
["java","sql","python","c","html"]
Output Example:
1 → [c]
3 → [sql]
4 → [java, html]
6 → [python]*/
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class Q1 {
    public static void main(String arg[]){

        ArrayList<String>words=new ArrayList<String>(Arrays.asList("java","sql","python","c","html"));
        
        Map<Integer,ArrayList<String>>map=new HashMap<Integer,ArrayList<String>>();
    
        for (String word:words){
            int length=word.length();
            if(!map.containsKey(length)){
                map.put(length,new ArrayList<String>());
            }
            map.get(length).add(word);
        }
        for(Integer key:map.keySet()){
            System.out.println(key+" → "+map.get(key));
        }
//Another way to do the same using Java 8 Stream API
    List<String> al = Arrays.asList("C", "Java", "HTML", "Python");

    Map<Integer,List<String>>map2=al.stream().collect(Collectors.groupingBy(String::length));
    System.out.println(""+map2);

    }


}

