import java.util.*;

public class Operation {

    
    static void operation(int[] arr) {
        Set<Integer> set = new LinkedHashSet<>();
        for (int num : arr) {
            set.add(num);
        }
        System.out.println("Distinct Elements = " + set);
    }

    static void operation(int[] arr1, int[] arr2) {
        Set<Integer> set1 = new LinkedHashSet<>();
        Set<Integer> set2 = new LinkedHashSet<>();

        for (int num : arr1) {
            set1.add(num);
        }
        for (int num : arr2) {
            set2.add(num);
        }

        Set<Integer> intersection = new LinkedHashSet<>(set1);
        intersection.retainAll(set2);

        Set<Integer> union = new LinkedHashSet<>(set1);
        union.addAll(set2);

        System.out.println("Intersection = " + intersection + ", Union = " + union);
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 2, 3, 4};
        operation(a);

        int[] b = {1, 2, 3};
        int[] c = {2, 3, 4};
        operation(b, c);
    }
}