package TechCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayApp {

	public static void main(String[] args) {
		List<Integer> list=Arrays.asList(1,2,3,4,5,6,7,8,9);
		List<Integer> list1=Arrays.asList(9,10);
		ArrayList<Integer>al=new ArrayList<>();
//		
//		Set<Integer> set= new HashSet<>(list);
		
		//System.out.println(set);
		
		/*
		 * Collections.sort(list);
		 * 
		 * System.out.println(list);
		 */
		//System.out.println(list.get(list.size()-2));
		
		/*
		 * Collections.sort(list,Collections.reverseOrder());
		 * 
		 * System.out.println(list);
		 */
		
		
		/*
		 * int max=Collections.max(list);
		 * 
		 * 
		 * for(int i=1;i<max;i++) { if(!list.contains(i)) { System.out.print(" "+i); }
		 * 
		 * }
		 */
		
		/*
		 * Map<Integer,Integer>map=new LinkedHashMap<>();
		 * 
		 * 
		 * for (int num:list) { map.put(num ,map.getOrDefault(num,0)+1); }
		 * 
		 * for(Map.Entry<Integer,Integer>entery:map.entrySet()) {
		 * if(entery.getValue()==1) {
		 * System.out.println("first unique number "+entery.getKey()); break;
		 * 
		 * }
		 * 
		 * }
		 */
		
		
		
		
		/*
		 * al.addAll(list); al.addAll(list1); System.out.println(al);
		 */
		
		//Alternate Merge
		/*
		 * int i=0,j=0; while(i<list.size() && j< list1.size() ) { al.add(list.get(i));
		 * al.add(list1.get(j)); i++; j++;
		 * 
		 * } while(i<list.size()) { al.add(list.get(i)); i++; } while(j<list1.size()) {
		 * al.add(list1.get(j)); j++; } System.out.println(" "+al);
		 
		 */
		
		/*
		 * Count characters in string Find pairs with given sum
		 * Count vowels/consonants
Character frequency
Reverse a list
Rotate list
Merge two lists
Find intersection of two lists
		 */
		
		
		
		
		/*
		 * String str="Harishchandra";
		 * 
		 * Set<Character>vowelset=new HashSet<>(Arrays.asList('a','e','i','u','o'));
		 * 
		 * 
		 * int vowel=0,constant=0; str=str.toLowerCase(); for(char ch:
		 * str.toCharArray()) { if(Character.isLetter(ch)) { if(vowelset.contains(ch)) {
		 * 
		 * 
		 * vowel++; } else { constant++;
		 * 
		 * } } }
		 * 
		 * System.out.println("vowel count is : "+vowel);
		 * System.out.println("Constants count is : "+constant);
		 */
		
		
		/*
		 * String str="haria4ntarkar1"; int count=0; for(char ch:str.toCharArray()) {
		 * if(Character.isLetter(ch)) { count++; } }
		 * System.out.println("characte count is "+count );
		 */
		/*
		 * int target=14; for(int i=0;i<list.size();i++) { for(int
		 * j=i+1;j<list.size();j++) {
		 * 
		 * 
		 * if(list.get(i)+list.get(j)==target) { System.out.println("Index: "+i+","+j);
		 * System.out.println("pair ("+list.get(i)+","+list.get(j)+")"); }} }
		 */
		
		/*
		 * Collections.reverse(list); System.out.println(list);
		 *
		 */
		
		/*
		 * for(int i=list.size()-1;i>=0;i--) { al.add(list.get(i)); }
		 * System.out.println(" "+al);
		 */
		
		/*
		 * Collections.rotate(list, 4); System.out.println(" "+list);
		 */
		int sum=0;
		for(int i=1;i<list.size();i++) {
			if(list.get(i)%2==1) {
				sum=sum+list.get(i);
			}
		}
		System.out.println("evan num sum is "+sum);
		
		
	}
}

