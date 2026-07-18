public class FindVpwelandConFromStr {
    public static void main(String x[]){
        String str="Harishchandra Shahadeo Antarkar".toLowerCase();

        String words[]=str.split(" ");
        for(String word:words){
        int vowelCount=0;
        int constantcount=0;
        for(int i=0;i<word.length();i++ ){
            char ch=word.charAt(i);
            if(ch >= 'a' && ch<= 'z'){
                if(ch=='a'||ch=='e'||ch=='i'||ch=='o'||ch=='u'){
                vowelCount++;
                 }
                else
                 {
                constantcount++;
                }
            }     
        }
            System.out.println("Word : " + word);
            System.out.println("Vowels : " +  vowelCount);
            System.out.println("Consonants : " +    constantcount);
            System.out.println("---------------------->");
    }
}
}






