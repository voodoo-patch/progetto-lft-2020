package EVALUATOR;

import java.io.*;
import LEXER.*;
import PARSER.*;

public class Evaluator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Evaluator(Lexer l, BufferedReader br) {
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
        int expr_val;
        expr_val = expr();
        match(Tag.EOF);
        System.out.println(expr_val);
    }

    private int expr() {
        int term_val, exprp_val;
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;
            default:
                exprp_val = 0;
                error("syntax error in exprp method");
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val;
        switch (look.tag) {
            case '+':
                match(Token.plus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match(Token.minus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case ')':
            case Tag.EOF:
                exprp_val = exprp_i;
                break;
            default:
                exprp_val = 0;
                error("syntax error in exprp method");
        }
        return exprp_val;
    }

    private int term() {
        int term_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            int fact_val = fact();
            term_val = termp(fact_val);
        } else {
            term_val = 0;
            error("syntax error in term method");
        }
        return term_val;
    }

    private int termp(int termp_i) {
        int fact_val, termp_val;
        switch (look.tag) {
            case '*':
                match(Token.mult.tag);
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match(Token.div.tag);
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case ')':
            case '+':
            case '-':
            case Tag.EOF:
                termp_val = termp_i;
                break;
            default:
                termp_val = 0;
                error("syntax error in termp method");
        }
        return termp_val;
    }

    private int fact() {
        int fact_val;
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                fact_val = expr();
                match(Token.rpt.tag);
                break;
            case Tag.NUM:
                fact_val = ((NumberTok)look).GetNumber();
                match(Tag.NUM);
                break;
            default:
                fact_val = 1;
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "INPUT/"; // il percorso del file da leggere
        path += (args != null && args.length > 0 && args[0] != null) ? args[0] : "dummy.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Evaluator evaluator = new Evaluator(lex, br);
            evaluator.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
