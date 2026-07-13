public class PalindromeNumber {
    public static void main (String []args){

        int num=121;

        int temp=num;
        int rev=0;
        while(temp>0){
            int lastdigit=temp%10;
            rev=rev*10 +lastdigit;
               temp=temp/10;
        }
        if(rev==num){
            System.out.println("number is palindrom ");
        }
        else{
            System.out.println("num is not palindrome ");
        }
       //another way using Stringbuilder 
        int nums=151;
        String originaltype=String.valueOf(nums);
        String sb=new StringBuilder(originaltype).reverse().toString();
        if (originaltype.equals(sb)) {
            System.out.println(originaltype +" : IS a palindrome number  ");
        }
        else{
            System.out.println(originaltype+" Is not a palindrome number");
        }
        //another way using stringbilder without inbuilt method...

        int n=1421;
        String str=String.valueOf(n);
        StringBuilder sb1=new StringBuilder();

        for(int i=str.length()-1;i>=0;i-- ){
            sb1.append(str.charAt(i));
        }
        if(str.equals(sb1.toString())){
            System.out.println(n+" IS a palindrome number.....");
        }
        else{
            System.out.println(n+" Is a not  palindrome number....");
        }
    }
}
