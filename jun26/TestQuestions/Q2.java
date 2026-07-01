/*Store N integers in an ArrayList. Rotate the elements to the right by one position.
Input:
5
10 20 30 40 50
Output:
Rotated List: 50 10 20 30 40
Description:
Store elements in ArrayList.
Move last element to first position manually.
Shift remaining elements one position forward */
import java.util.*;
public class Q2 {
    public static void main(String[]arg){

        ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(10, 20, 30, 40, 50));

        int lastElement=al.get(al.size()-1);
        al.remove(al.size()-1);
        al.add(0,lastElement);
        System.out.println("Rotated List: " + al);

        //Another way to do the same using Collections.rotate() method
                ArrayList<Integer> al2 = new ArrayList<Integer>(Arrays.asList(1, 20, 30, 40, 50));

                Collections.rotate(al2, 1);
        System.out.println("Rotated List: " + al2);
    }
}
