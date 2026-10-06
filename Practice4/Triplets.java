import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Triplets {

    public static void main(String[] args) {

        int[] arr = {1, 2, -1, 0, -2, 1, 3, 4};

        int target = 3;

        Set<String> uniqueTriplets = new HashSet<>();

        for (int i = 0; i < arr.length - 2; i++) {

            for (int j = i + 1; j < arr.length - 1; j++) {

                for (int k = j + 1; k < arr.length; k++) {

                    int sum = arr[i] + arr[j] + arr[k];

                    if (sum == target) {

                        int a = arr[i];
                        int b = arr[j];
                        int c = arr[k];

                        if (a > b) {
                            int temp = a;
                            a = b;
                            b = temp;
                        }

                        if (b > c) {
                            int temp = b;
                            b = c;
                            c = temp;
                        }

                        if (a > b) {
                            int temp = a;
                            a = b;
                            b = temp;
                        }

                        String key = a + "," + b + "," + c;

                        uniqueTriplets.add(key);
                    }
                }
            }
        }

        System.out.println("Triplets:");

        for (String triplet : uniqueTriplets) {
            System.out.println(triplet);
        }
    }
}