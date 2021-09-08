package AUTOMATON;

public class Automaton11 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == 'a' || ch == '*')
                        state = 0;
                    else if (ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch == 'a' || ch == '/')
                        state = 1;
                    else if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == 'a' || ch == '/')
                        state = 2;
                    else if (ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a')
                        state = 2;
                    else if (ch == '/')
                        state = 0;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 0 || state == 1;
    }

    public static void main(String[] args) {
        // args = new String[] { "aaa/****/aa", "aa/*a*a*/", "aaaa", "/****/", "/*aa*/", "*/a", "a/**/***a", "a/**/***/a",
        //         "a/**/aa/***/a" };
        // for (String test : args) {
        //     System.out.println(scan(test) ? "OK" : "NOPE - " + test);
        // }
        // System.out.println("----------");
        // args = new String[] { "aaa/*/aa", "a/**//***a", "aa/*aa" };
        // for (String test : args) {
        //     System.out.println(scan(test) ? "OK" : "NOPE - " + test);
        // }

        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}