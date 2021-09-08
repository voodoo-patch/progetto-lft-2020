package AUTOMATON;

public class Utils {
    public static boolean IsLetter(char x) {
        return IsLowercase(x) || IsUppercase(x);
    }

    public static boolean IsUppercase(char x) {
        return x >= 65 && x <= 90;
    }

    public static boolean IsLowercase(char x) {
        return x >= 97 && x <= 122;
    }

    public static boolean IsFirstHalf(char x) {
        return (x >= 65 && x <= 75) || (x >= 97 && x <= 107);
    }

    public static boolean IsSecondHalf(char x) {
        return (x >= 76 && x <= 90) || (x >= 108 && x <= 122);
    }

    public static boolean IsNumber(char x) {
        return x >= 48 && x <= 57;
    }

    public static boolean IsOdd(char x) {
        return IsNumber(x) && (int) x % 2 != 0;
    }

    public static boolean IsEven(char x) {
        return IsNumber(x) && (int) x % 2 == 0;
    }

    public static boolean IsUnderscore(char x) {
        return x == 95;
    }

    public static boolean IsWhitespace(char x) {
        return x == 32;
    }
}