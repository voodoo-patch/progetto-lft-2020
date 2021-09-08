package LEXER;

public class Word extends Token {
    public String lexeme = "";

    public Word(int tag, String s) {
        super(tag);
        lexeme = s;
    }

    public static Word GetIdentifier(String s) {
        switch (s) {
            case "cond":
                return cond;
            case "when":
                return when;
            case "then":
                return then;
            case "else":
                return elsetok;
            case "while":
                return whiletok;
            case "do":
                return dotok;
            case "print":
                return print;
            case "read":
                return read;
            default:
                if (s.contains("@") || s.contains("#")) {
                    System.err.println("Erroneous character in identifier: " + s);
                    return null;
                }
                if (s.replace("_", "").length() == 0) {
                    System.err.println("Identifier cannot contain only '_' char: " + s);
                    return null;
                }
                return new Word(Tag.ID, s);
        }
    }

    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }

    public static final Word cond = new Word(Tag.COND, "cond");
    public static final Word when = new Word(Tag.WHEN, "when");
    public static final Word then = new Word(Tag.THEN, "then");
    public static final Word elsetok = new Word(Tag.ELSE, "else");
    public static final Word whiletok = new Word(Tag.WHILE, "while");
    public static final Word dotok = new Word(Tag.DO, "do");
    public static final Word print = new Word(Tag.PRINT, "print");
    public static final Word read = new Word(Tag.READ, "read");
    public static final Word or = new Word(Tag.OR, "||");
    public static final Word and = new Word(Tag.AND, "&&");
    public static final Word not = new Word(Tag.NOT, "!");
    public static final Word lt = new Word(Tag.RELOP, "<");
    public static final Word gt = new Word(Tag.RELOP, ">");
    public static final Word eq = new Word(Tag.RELOP, "==");
    public static final Word le = new Word(Tag.RELOP, "<=");
    public static final Word ne = new Word(Tag.RELOP, "<>");
    public static final Word ge = new Word(Tag.RELOP, ">=");
}