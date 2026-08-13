public class QValidNumber65 {
    public static void main(String []args){
        String str="12.32";
        boolean  b=isNumber(str);
        if(b){
            System.out.println(b+" : Number is valid ..");
        }
        else{
            System.out.println(b+" : number is not valid ..." );
        }
    }
     public static boolean isNumber(String s) {
           boolean digitSeen = false;
        boolean dotSeen = false;
        boolean eSeen = false;
        boolean digitAfterE = true;

        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            if (Character.isDigit(ch)) {
                digitSeen = true;
                digitAfterE = true;
            }

            else if (ch == '.') {

                if (dotSeen || eSeen)
                    return false;

                dotSeen = true;
            }

            else if (ch == 'e' || ch == 'E') {

                if (eSeen || !digitSeen)
                    return false;

                eSeen = true;
                digitAfterE = false;
            }

            else if (ch == '+' || ch == '-') {

                if (i != 0 && s.charAt(i - 1) != 'e' && s.charAt(i - 1) != 'E')
                    return false;
            }

            else {
                return false;
            }
        }

        return digitSeen && digitAfterE;
    } 
     //return s.matches("[+-]?((\\d+\\.?\\d*)|(\\.\\d+))([eE][+-]?\\d+)?");}
}
