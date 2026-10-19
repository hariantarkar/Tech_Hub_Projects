import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
public class UseBufferedWritter {
    public static void main(String []args){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter("c:\\Tech_Hub_Projects\\NewFile.txt"))){
            bw.write("this is an testing data..");
            bw.newLine();
            bw.write("using the bufferedwritter classs ways");
          
        }catch(Exception e){
            System.out.println("An error occured during file creation "+e.getMessage());
            e.printStackTrace();

        }  
}
}
