package AUTOMATON;

//Matricole dei turni 2 e 3 - cognome+matricola
public class Automaton5 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            case 0:
                if (Utils.IsFirstHalf(ch))
                    state = 1;
                else if (Utils.IsSecondHalf(ch))
                    state = 4;
                else
                    state = -1;
                break;
            case 1:
                if (Utils.IsLetter(ch))
                    state = 1;
                else if (Utils.IsEven(ch))
                    state = 2;
                else if (Utils.IsOdd(ch))
                    state = 3;
                else
                    state = -1;
                break;
            case 2:
                if (Utils.IsEven(ch))
                    state = 2;
                else if (Utils.IsOdd(ch))
                    state = 3;
                else
                    state = -1;
                break;
            case 3:
                if (Utils.IsOdd(ch))
                    state = 3;
                else if (Utils.IsEven(ch))
                    state = 2;
                else
                    state = -1;
                break;
            case 4:
                if (Utils.IsLetter(ch))
                    state = 4;
                else if (Utils.IsEven(ch))
                    state = 6;
                else if (Utils.IsOdd(ch))
                    state = 5;
                else
                    state = -1;
                break;
            case 5:
                if (Utils.IsEven(ch))
                    state = 6;
                else if (Utils.IsOdd(ch))
                    state = 5;
                else
                    state = -1;
                break;
            case 6:
                if (Utils.IsOdd(ch))
                    state = 5;
                else if (Utils.IsEven(ch))
                    state = 6;
                else
                    state = -1;
                break;
            }
        }
        return state == 2 || state == 5;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}