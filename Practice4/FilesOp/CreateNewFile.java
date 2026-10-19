import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CreateNewFile{
    public static void main(String []args){
        File f=new File("c:\\Tech_Hub_Projects");

        try{
           /*  if(f.createNewFile()){
                System.out.println("file  created successfully");
            }
            else{
                System.out.println("Failed to create the file.");
            }
           if(f.canRead()){
            System.out.println("yes file is  readable  ...");
           }
          if(f.canWrite()){
            System.out.println("yes file is  writable  ...");
           }*/
           /*try(FileWriter writer =new FileWriter(f)){
            writer.write("this is a new data wrtitten in new file ...");
            System.out.println(" data written successfully in new file ...");
            writer.close();
           }
           Scanner sc=new Scanner(f);
           while(sc.hasNextLine()){
            String data=sc.nextLine();         
               System.out.println(data);
           }
           if(f.delete()){
            System.out.println("file deletes successfully..."+f.getName()+"..path is.."+f.getAbsolutePath());
           }*/

            File files[]=f.listFiles();
            for(int i=0;i<files.length;i++){
                if(files[i].isDirectory()){
                    System.out.println(" "+files[i]);
                }
                
            }
        }catch(Exception e){
            System.out.println("An error occured during file creation "+e.getMessage());

            e.printStackTrace();
        }//what does the e.printStackTrace() do? it prints the error message in the console 
        //what happen if we remove the e.printStackTrace() from the catch block? it will not print the error message in the console

    }
}

