public class StringDigitSum {
    public static void main(String[] args) {
        String str = "abc123def456";
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                sum += Character.getNumericValue(ch);
            }
        }
        System.out.println("Sum of digits in the string: " + sum);
        String str1 = "abc123def4567";
        System.out.println("Sum of digits manual calculation : " + sumOfDigits(str1));

         String str2 = "abc123def4567f5";
        System.out.println("Sum of digits manual calculation another way  : " + sumOfD(str2));
   
    } 
    public static int sumOfDigits(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                count += (ch - '0');
            }
        }
        return count;

    }
    public static int sumOfD(String st){
        int countsum=0;
        for(int i=0;i<st.length();i++){
            char ch=st.charAt(i);
            if(ch>='0' && ch <='9'){
                int ascii=(int)ch;
                countsum+=ascii-48;
            }
        }
        return countsum;
    }
}
