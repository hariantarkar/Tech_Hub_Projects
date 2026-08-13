/* Store N strings in an ArrayList. Remove duplicate strings manually and store unique strings in
another ArrayList.
Input:
java code java list code map
Output:
Unique List: java code list map
Description:
Traverse original ArrayList.
For each element, check manually in second ArrayList using loop comparison */
import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;

public class Q3 {
   public static void main(String []arg){

    ArrayList<String>al=new ArrayList<String>(Arrays.asList("java","cpp","python","node.js","cpp"));

    ArrayList<String>uniqueList=new ArrayList<String>();

    for (String str:al){
        boolean isDuplicate=false;
        for(String st1:uniqueList){
            if(str.equals(st1)){
                isDuplicate=true;
            break;
            }
            
        }
        if(!isDuplicate){
            uniqueList.add(str);
        }
    }
    System.out.println("Unique List: " + uniqueList);
   
   //another way to use HashSet to remove duplicates
       ArrayList<String>al2=new ArrayList<String>(Arrays.asList("java","cpp","python","node.js","cpp","java"));
 
       Set<String>set=new LinkedHashSet<>();

       for (String str:al2){
        if(!set.contains(str)){
            set.add(str);
        }

       }
       System.out.println("Unique List find second way : " + set);
//another way to use linkedHashset but pass direct list to constructor
       ArrayList<String>al3=new ArrayList<String>(Arrays.asList("java","cpp","python","node.js","cpp","python"));
 
       Set<String>set2=new LinkedHashSet<>(al3);

       System.out.println("Unique List find third way : " + set2);
   }
   
}
