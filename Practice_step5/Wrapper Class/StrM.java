public class StrM {
    public static void main(String x[]){
        String s="    abc";

        String s2="mno    ";

        System.out.println(s+ " :  s value hashcode: "+  System.identityHashCode(s));

        System.out.println(s2+":  s2 value  hashcode: "+ System.identityHashCode(s2));

        s=s.concat(s2);
        System.out.println(s);
            System.out.println(s+ " :  s value hashcode: "+  System.identityHashCode(s));
        System.out.println(s.trim());

    }
}
