public class B extends Thread{

    public void run(){
        try{
        for(int i=5;i<=10;i++){
            System.out.println("J value : "+i + isAlive());
            Thread.sleep(1000);
        }
    }catch(Exception e){
        System.out.println("error facing during perfrom the OP. "+e.getMessage());
        
    }
    }

}