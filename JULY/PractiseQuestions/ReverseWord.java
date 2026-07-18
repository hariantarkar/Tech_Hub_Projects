public class ReverseWord {
    public static void main(String []args){

        String  str="java is a  programming language";

        String words[]=str.split(" ");

        StringBuilder result=new StringBuilder();
        for(String word:words){

            if(!word.isEmpty()){
                for(int i=word.length()-1;i>=0;i--){
                    result.append(word.charAt(i));
                }
                result.append(" ");
            }
            
        }
        String newstr=result.toString();
        System.out.println(newstr);
    }
}


