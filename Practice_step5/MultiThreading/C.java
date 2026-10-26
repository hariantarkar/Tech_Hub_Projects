public class C extends Thread{
    public void run(){

         for(int i=5;i<=10;i++){
            System.out.println("I value : "+i  + isAlive());
            Thread.sleep(1000);

        }
    }
} 
