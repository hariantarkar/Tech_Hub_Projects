public class StringFirstWordC {
    public static void main(String[]args){
    
        String Str="java is a programming language ";
        String words[]=Str.split(" ");
     
        for(String word:words){
           if(word.length()>0){
            String firstchar=word.substring(0,1);
          String   result=firstchar.toUpperCase().concat(word.substring(1));
            
            System.out.print(result+" ");
           } 
        }

        String str="hari s  antarkar ";

        String words1[]=str.split( " ");
        StringBuilder newstr=new StringBuilder();

        for(String word:words1){
            if(!word.isEmpty()){

                newstr.append(word.substring(0,1).toUpperCase())
                .append(word.substring(1))
                .append(" ");
            }
        }
        System.out.println("\n"+newstr);

    }
}
