import java.util.Collection;

public class PalindromeString {
   public static void main(String []args){
    String str="nitin";
    String temp=str,result="";
    for(int i=temp.length()-1;i>=0;i--){
        result+=temp.charAt(i);

    }
    if(str.equals(result)){
        System.out.println(str+" : Is a palindrom string ");
    }
    else{
        System.out.println(str+" : Is a not palindrome string ..");
    }
    
   } 
}
