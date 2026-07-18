public class StringOp {
    public String Test(String str,String str1){
        String sb=str+str1;
        return sb;
    }
    public static void main(String[] args) {
        StringOp s=new StringOp();

        String result=s.Test("java","is");
        System.out.println(result);
    }
}
