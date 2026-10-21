import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UseTransient implements Serializable{
    private  int id=01;
    private String name="hari";
    private String Address="Beed";
    private transient int age =23;
    
    public static void main(String []args){

        UseTransient ut=new UseTransient();
        try{
            
        
        FileOutputStream fout=new FileOutputStream("c:\\Tech_Hub_Projects\\NewFile.txt");

    ObjectOutputStream oout =new ObjectOutputStream(fout);
    oout.writeObject(ut);

    oout.close();
    FileInputStream fin=new FileInputStream("c:\\Tech_Hub_Projects\\NewFile.txt");
    ObjectInputStream oin=new ObjectInputStream(fin);

    UseTransient ut1=(UseTransient)oin.readObject();
    System.out.println("id is.."+ut1.id);
    System.out.println("name is.."+ut1.name);
    System.out.println("Address is.."+ut1.Address);
    System.out.println("age is.."+ut1.age);
    oin.close();



  
        }catch(Exception e){
            System.out.println("An error occured during file OP "+e.getMessage());
            e.printStackTrace();
        }
    } 
    
} 
