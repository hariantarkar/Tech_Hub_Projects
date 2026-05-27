// Write a program to check whether an array is palindrome or not
package Arrays.may.code;
import java.util.Scanner;

public class PalindromeArrays {

	public static void main(String[] args) {
			
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter Size : ");
				int size = sc.nextInt();
				int arr[] = new int[size];
				System.out.print("Enter "+size+" elements: ");
				for(int i = 0; i<size; i++)
				{
					arr[i] = sc.nextInt();
				}
				System.out.print("Array : ");
				for(int i = 0; i<size; i++)
				{
					System.out.print(arr[i]+" ");
				}
				int flag = 0;
				for(int i = 0, j = arr.length-1; i<(int) (arr.length)/2; i++, j--)
				{
					if(arr[i]==arr[j])
					{
						flag = 1;
					}
					else
					{
						flag = 0;
					}
				}
				if(flag==1)
				{
					System.out.println("Array is Palindrome");
				}
				else
				{
					System.out.println("Array is not Palindrome");
				}
			
	}

}


