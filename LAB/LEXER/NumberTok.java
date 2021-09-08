package LEXER;

public class NumberTok extends Token {
    private int Number;

    public int GetNumber(){
        return Number;
    }

    public NumberTok(int number) {
        super(Tag.NUM);
        Number = number;
    }

    public String toString() {
        return "<" + Tag.NUM + ", " + this.Number + ">";
    }
}