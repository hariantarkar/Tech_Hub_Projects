public class ReverseString {
 public static void main(String []args){
/*Reverse each word "java is" → "avaj si" */
    String str="java is a programming language ".trim();
    String str1="java is a programming language ".trim();

StringBuilder result=new StringBuilder();
    for(int i=str.length()-1;i>=0;i--){
      result.append(str.charAt(i));  
    }
    System.out.println(result);;
  /* Reverse the order of words "java is a programming language" → "language programming a is java */
 StringBuilder result1=new StringBuilder();

 String words[]=str1.split(" ");
 for(int i=words.length-1;i>=0;i--){
    result1.append(words[i]+" ");

 }
 System.out.println(result1);

}
}
