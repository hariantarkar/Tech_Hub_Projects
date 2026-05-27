package TechCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Two {

	public static void main(String[] args) {
		
		
		List<Integer> list= Arrays.asList(1,3,2,1,4,6,8,9,9,15);
		Integer a[]= {1,3,2,4,3,5};
		
		/*
		 * Set<Integer>set=new LinkedHashSet<Integer>(Arrays.asList(a));
		 * 
		 * Set<Integer>set1=new LinkedHashSet<Integer>(list);
		 */
		/*
		 * System.out.println("after remove the duplicate element of the  array "+set);
		 * System.out.println("after removing duplicate element of the list "+set1);
		 */
		
		 int max=Collections.max(list);
		 Set<Integer>set1=new LinkedHashSet<Integer>(list);
		 for(int i=1;i<max;i++) 
		 { 
			 if(!set1.contains(i)) 
			 {
				 System.out.println(" "+i); 
			}
		}
		 
		
	}

}
