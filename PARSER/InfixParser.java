package PARSER;

import java.io.*;

import LEXER.*;

public class InfixParser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public InfixParser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() {
        if (look.tag == Tag.NUM || look.tag == '(') {
            expr();
            match(Tag.EOF);
        } else
            error("syntax error in start method");
    }

    private void expr() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term();
                exprp();
                break;
            default:
                error("syntax error in exprp method");
        }
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match(Token.plus.tag);
                term();
                exprp();
                break;
            case '-':
                match(Token.minus.tag);
                term();
                exprp();
                break;
            case ')':
            case Tag.EOF:
                break;
            default:
                error("syntax error in exprp method");
        }
    }

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else
            error("syntax error in term method");
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match(Token.mult.tag);
                fact();
                termp();
                break;
            case '/':
                match(Token.div.tag);
                fact();
                termp();
                break;
            case ')':
            case '+':
            case '-':
            case Tag.EOF:
                break;
            default:
                error("syntax error in termp method");
        }
    }

    private void fact() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                expr();
                match(Token.rpt.tag);
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case ')':
            case Tag.EOF:
                break;
            default:
                error("syntax error in exprp method");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "INPUT/"; // il percorso del file da leggere
        path += (args != null && args.length > 0 && args[0] != null) ? args[0] : "dummy.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            InfixParser parser = new InfixParser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}