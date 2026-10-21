
public class TestMain {
    public static void main(String args[]) {
        A a =new A();

        a.start();
        try{
            a.join();
        System.out.println("current status is the thread  .."+a.isAlive());

        }catch(InterruptedException e){
            System.out.println("Main thread is interrupted during the OP. "+e.getMessage());
            Thread.currentThread().interrupt();
            return;
        }

       
        B b =new B();

        b.start();
        System.out.println("current status is the thread  .."+b.isAlive());


    }


}

