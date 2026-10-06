import java.util.ArrayList;

public class MergeArrayList {

    public static void main(String[] args) {

        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(10);
        list1.add(30);
        list1.add(20);
        list1.add(50);
        list1.add(30);

        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(40);
        list2.add(20);
        list2.add(60);
        list2.add(10);

        ArrayList<Integer> result = new ArrayList<>();

        for (Integer num : list1) {
            if (!result.contains(num)) {
                result.add(num);
            }
        }

        for (Integer num : list2) {
            if (!result.contains(num)) {
                result.add(num);
            }
        }

        for (int i = 0; i < result.size() - 1; i++) {

            for (int j = 0; j < result.size() - 1 - i; j++) {

                if (result.get(j) > result.get(j + 1)) {

                    Integer temp = result.get(j);

                    result.set(j, result.get(j + 1));

                    result.set(j + 1, temp);
                }
            }
        }

        System.out.println(result);
    }
}