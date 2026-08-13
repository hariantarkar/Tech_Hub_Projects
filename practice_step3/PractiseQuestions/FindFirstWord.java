public class FindFirstWord {
    public static void main(String []args){
        String str="Harishchandra Shahadeo Antarkar";

        String names[]=str.split(" ");
        StringBuilder sb=new StringBuilder();
        for(String word:names){
            if(!word.isEmpty()){
                sb.append(word.substring(0, 1));
            }
            sb.append(" ");
        }
        System.out.println(sb);
    }
}

