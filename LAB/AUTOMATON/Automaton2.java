package AUTOMATON;

//Java Identifiers
public class Automaton2 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            case 0:
                if (Utils.IsLetter(ch))
                    state = 2;
                else if (Utils.IsUnderscore(ch))
                    state = 1;
                else
                    state = -1;
                break;
            case 1:
                if (Utils.IsUnderscore(ch))
                    state = 1;
                else if (Utils.IsLetter(ch) || Utils.IsNumber(ch))
                    state = 2;
                else
                    state = -1;
                break;
            case 2:
            if (Utils.IsLetter(ch) || Utils.IsNumber(ch) || Utils.IsUnderscore(ch))
                    state = 2;
                else
                    state = -1;
                break;
            }
        }
        return state == 2;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}