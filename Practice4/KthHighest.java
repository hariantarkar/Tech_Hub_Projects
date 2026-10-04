import java.util.*;

public class KthHighest {

    int findKthHighest(int[] arr, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : arr) {
            minHeap.add(num);
            if (minHeap.size() > k) {
                minHeap.poll();   
            }
        }
        return minHeap.peek();
    }

    public static void main(String[] args) {
        KthHighest obj = new KthHighest();
        int[] arr = {12, 5, 7, 20, 3, 15};
        int k = 3;

        int result = obj.findKthHighest(arr, k);
        System.out.println(k + "rd highest element = " + result);
    }
}