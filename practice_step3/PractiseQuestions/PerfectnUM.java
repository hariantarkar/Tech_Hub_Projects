public class PerfectnUM {
    public static void main(String x[]){
       int num=6,sum=0;

        for(int i=1;i<num;i++){
            if(num%i==0){
                sum+=i;
            }

        }
        if(sum==num){
            System.out.println("number is perfect num");
        }
        else{
            System.out.println("num is not perfect");
        }
    }
}
