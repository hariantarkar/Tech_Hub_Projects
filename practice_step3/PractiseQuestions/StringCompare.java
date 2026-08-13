public class StringCompare {
    
    public static void main(String[]args){

        String str1="sham";
        String str2="sham";
        if(str1.equals(str2)){
            System.out.println("Strings are equals");

        }else{
            System.out.println("Strings are not equals");
        }


        String str3="shamesh";
        String str4="shamesh";
        if(str3!=str4){
            System.out.println("String are not equals");

        }else{
            boolean result=true;
            for(int st=0;st<str3.length();st++){
                if(str3.charAt(st)!=str4.charAt(st)){
                    result =false;
                    break;
                }

            }
            if(result){
                System.out.println("Strings are equals");
            }
            else{
                System.out.println("Strings are not equals");
            }
        }
    }
}
