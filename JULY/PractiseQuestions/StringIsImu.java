public class StringIsImu {
    public static void main(String x[]){
        String str="abc";
        System.out.println(System.identityHashCode(str));
        str="xyz";
                System.out.println(System.identityHashCode(str));

        System.out.println(str);

    }
}
















