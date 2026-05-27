package Arrays.may.code;

import java.util.Scanner;

public class InserEleArray {

	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		int a[]=new int[6];
		System.out.print("enter array elements ");
		for(int i=0;i<a.length-1;i++) {
			a[i]=sc.nextInt();
		}
		System.out.print("enetr element which you want to insert in array");
		int element=sc.nextInt();
	
		System.out.print("enter index where you want to insert element in array");
		int index=sc.nextInt();
	
		for(int i=a.length-1;i>index;i--) {
			a[i]=a[i-1];
			
		}
		a[index]=element;
		for(int i=0;i<a.length;i++) {
			System.out.print(a[i]+"\t");
		}
	}
	

}
