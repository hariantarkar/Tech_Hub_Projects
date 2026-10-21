public class UseFinalizeM {
     protected void finalize() {
        System.out.println("Object is destroyed");
    }
    public static void main(String args[]){
        UseFinalizeM ufm=new UseFinalizeM();
        ufm=null;
        System.gc();
    }
}
