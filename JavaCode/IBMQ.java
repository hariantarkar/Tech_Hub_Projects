import java.util.List;
public class IBMQ {
    
    public static String plusMult(List<Integer> A) 
    {
        long even = A.get(0);
        long odd = A.get(1);

    for (int i = 2; i < A.size(); i += 4) {
        even = (even * A.get(i)) % 2;

        if (i + 2 < A.size()) {
            even = (even + A.get(i + 2)) % 2;
        }
    }
    for (int i = 3; i < A.size(); i += 4) 
    {
        odd = (odd * A.get(i)) % 2;
        if (i + 2 < A.size()) {
            odd = (odd + A.get(i + 2)) % 2;
        }
    }
    if (even > odd) {
        return "ODD";
    } else if (even < odd) {
        return "EVEN";
    } else {
        return "NEUTRAL";
    }
    }
}

