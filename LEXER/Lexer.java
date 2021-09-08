package LEXER;

import java.io.*;

public class Lexer {
    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }
        switch (peek) {
            case '!':
                peek = ' ';
                return Word.not;
            case '(':
                peek = ' ';
                return Word.lpt;
            case ')':
                peek = ' ';
                return Word.rpt;
            case '{':
                peek = ' ';
                return Word.lpg;
            case '}':
                peek = ' ';
                return Word.rpg;
            case '+':
                peek = ' ';
                return Word.plus;
            case '-':
                peek = ' ';
                return Word.minus;
            case '*':
                peek = ' ';
                return Word.mult;
            case '/':
                readch(br);
                if (peek == '/') { // line comment
                    do {
                        readch(br);
                    } while (peek != '\n' && peek != '\r' && peek != (char) -1);
                    return lexical_scan(br);
                } else if (peek == '*') { // block comment
                    do {
                        while (peek != '*') {
                            readch(br);
                            if (peek == (char) -1) {
                                System.err.println("Unclosed block comment");
                                return null;
                            }
                        }
                        readch(br);
                        if (peek == (char) -1) {
                            System.err.println("Unclosed block comment");
                            return null;
                        }
                    } while (peek != '/');
                    peek = ' ';
                    return lexical_scan(br);

                } else {
                    return Word.div;
                }
            case ';':
                peek = ' ';
                return Word.semicolon;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character after & : " + peek);
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character after | : " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }
            case (char) -1:
                return new Token(Tag.EOF);
            default:
                if (Character.isLetter(peek) || peek == '_') { // isLetter does not include underscore char
                    String identifierString = Character.toString(peek);
                    readch(br);
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        identifierString += peek;
                        readch(br);
                    }
                    return Word.GetIdentifier(identifierString);
                } else if (Character.isDigit(peek)) {
                    String numberAsStr = "";
                    do {
                        numberAsStr += Character.toString(peek);
                        readch(br);
                    } while (Character.isDigit(peek));
                    return new NumberTok(Integer.parseInt(numberAsStr));
                } else {
                    System.err.println("Erroneous character: " + peek);
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "INPUT/"; // il percorso del file da leggere
        path += (args != null && args.length > 0 && args[0] != null) ? args[0] : "dummy.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
