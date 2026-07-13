public class StringQ {
    public static void main(String args[])
{

    char ch[]=new char[]{'a','b','c','d','e'};

    String str=new String(ch);
    String str1=new String(new char[]{'a','d','f','b','w'});

    byte b[]=new byte[]{97,100,101,102,103,102};

    String str2=new String(b);
        String str3=new String(new byte[]{97,100,101,102,103,102});


    System.out.println(str);
    System.out.println(str1);

    System.out.println(str2);
    System.out.println(str3);

    String str4="     hariantarkar2@gmail.com,harishantarkar3738@gmail.com,harshantarkar01@gmail.com    ";
String str5=str4.trim();
    String emails[]=str5.split(",");
    for(String email:emails){

        System.out.println(email);
    }

}  
}









