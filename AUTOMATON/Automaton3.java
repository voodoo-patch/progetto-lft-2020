package AUTOMATON;

//Matricole dei turni 2 e 3 - matricola+cognome
public class Automaton3 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            case 0:
                if (Utils.IsOdd(ch))
                    state = 2;
                else if (Utils.IsEven(ch))
                    state = 1;
                else
                    state = -1;
                break;
            case 1:
                if (Utils.IsEven(ch))
                    state = 1;
                else if (Utils.IsOdd(ch))
                    state = 2;
                else if (Utils.IsFirstHalf(ch))
                    state = 3;
                else
                    state = -1;
                break;
            case 2:
                if (Utils.IsEven(ch))
                    state = 1;
                else if (Utils.IsOdd(ch))
                    state = 2;
                else if (Utils.IsSecondHalf(ch))
                    state = 3;
                else
                    state = -1;
                break;
            case 3:
                if (Utils.IsLetter(ch))
                    state = 3;
                else
                    state = -1;
                break;
            }
        }
        return state == 3;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}