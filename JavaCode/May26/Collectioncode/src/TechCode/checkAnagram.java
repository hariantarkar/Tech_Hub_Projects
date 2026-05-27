package TechCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class checkAnagram {

	public static void main(String[] args) {
		String str="listsn";
		String str1="silent";
		
		if(str.length()!=str1.length()) {
			System.out.println("String is not Anagram");
			return;
			
		}
		/*
		 * char[] ch=str.toCharArray(); char[]ch1=str1.toCharArray();
		 * 
		 * Arrays.sort(ch); Arrays.sort(ch1); if(Arrays.equals(ch, ch1)) {
		 * System.out.println("String is anagram"); } else {
		 * System.out.println("String is not anagram"); }
		 */
//Using ArrayList + Collections.sort()
		
		List<Character> list=new ArrayList<>();
		
		List<Character>list1=new ArrayList<>();
		
		for(char ch:str.toCharArray()) {
			list.add(ch);
		}
		for(char ch:str1.toCharArray()) {
			list1.add(ch);
			
		}
		
		Collections.sort(list);
		Collections.sort(list1);
		
		if(list.equals(list1)) {
			System.out.println("String is anagram ");
		}
		else {
			System.out.println("String is not anagram");
			
		}
		
		
		
	}

}
