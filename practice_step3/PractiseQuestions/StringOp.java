public class StringOp {
    public String Test(String str,String str1){
        String sb=str+str1;
        return sb;
    }
    public static void main(String[] args) {
        StringOp s=new StringOp();

        String result=s.Test("java","is");
        System.out.println(result);

        String ex="a";
  
        for(int i=0;i<=4;i++){
            ex+=i;
            System.out.println(ex+" [Hashcode]: "+System.identityHashCode(ex));
        }
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<=4;i++){
            sb.append(i);
            System.out.println(sb+" [Hashcode]: "+System.identityHashCode(sb));
        }

        StringBuffer cap=new StringBuffer();

        System.out.println(cap.capacity());

    }
}
