package LEXER;
public class Token {
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return "<" + tag + ">";
    }

    //public static final Token not = new Token('!');
    public static final Token lpt = new Token('(');
    public static final Token rpt = new Token(')');
    public static final Token lpg = new Token('{');
    public static final Token rpg = new Token('}');
    public static final Token plus = new Token('+');
    public static final Token minus = new Token('-');
    public static final Token mult = new Token('*');
    public static final Token div = new Token('/');
	public static final Token assign = new Token('=');
    public static final Token semicolon = new Token(';');
}