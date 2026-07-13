public class LargestOddNumS {
    public static void main(String []args){
        int num=24680;
        String nums=String.valueOf(num);
        System.out.println(largestOddNumber(nums));
    }
     public static String largestOddNumber(String num) {
      
        for (int i = num.length() - 1; i >= 0; i--) {

            char ch = num.charAt(i);

            if ((ch - '0') % 2 == 1) {
                return num.substring(0, i + 1);
            }
        }

        return "There are no odd numbers in "+num;  
    }
}
