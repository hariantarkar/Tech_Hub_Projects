public class TestMain {
    public static void main(String []args) {
        Test t=new Test();
        try{
            t.show();
        }catch(Exception e){
            System.out.println("An error occured during perfrom the OP "+e.getMessage());
           
        } 
    }
}
