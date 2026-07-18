public class PrimeNum {
    public static void main(String x[]){
         int num=7,count=0;
         for(int i=1;i<=num;i++){
            if(num%i==0){
                count++;
            }
           
         } 
         if (count==2) {
                System.out.println("number is prime num");                
            }
            else{
                System.out.println("number is not prime ");
            }
    }
}
