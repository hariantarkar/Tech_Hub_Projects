import java.io.*;
public class ReadFileData {
    public static void main(String []args){
    
    
        try{
                 FileReader fr=new FileReader("c:\\Tech_Hub_Projects\\NewFile.txt");

            /*int Data;
            while((Data=fr.read())!=-1){
                System.out.print((char)Data);
                
            }*/
           BufferedReader br=new BufferedReader(fr);
           String data;
           while((data=br.readLine())!=null){
                System.out.println(data);
           }

        }catch(Exception e){
            System.out.println("An error occured during file creation "+e.getMessage());
            e.printStackTrace();
        }
        
    }
}





