public class MethodOverl {
    public static void test(String str){
        System.out.println("test method for string parameter ");
    }
    public static void test(Object obj){
        System.out.println("test method with object parameter...");
    }
    public static void main(String x[]){
        test(null);
    }
}
