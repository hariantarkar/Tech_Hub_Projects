package TechCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringPalindrome {

	public static void main(String[] args) {
		
		/*
		 * String str="nitin";
		 * 
		 * String reverse="";
		 * 
		 * for(int i=str.length()-1;i>=0;i--) { reverse=reverse+str.charAt(i);
		 * 
		 * } if(str.equals(reverse)) { System.out.println("String is palindrome "); }
		 * else { System.out.print("String is not palindrome"); }
		 */
		
		String str="121";
		
		String str1=str.valueOf(str);
		
		List<Character> list=new ArrayList<>();
		
		for(char ch:str.toCharArray()) {
			
			list.add(ch);
		}
		
		List<Character>reverse=new ArrayList<>(list);
		
		Collections.reverse(list);
		
		if(list.equals(reverse)) {
			System.out.println("string is palindrom:");
		}
		else
		{
			System.out.println("String is not palindrome");
		}

	}

}
