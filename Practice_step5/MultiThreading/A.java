public class A extends  Thread{

    public void run(){
        try{
        for(int i=1;i<=5;i++){
            System.out.println("I value : "+i + isAlive());
            Thread.sleep(1000);
        }
    }catch(Exception e){
        System.out.println("error facing during perfrom the OP. "+e.getMessage());

    }
    }
}