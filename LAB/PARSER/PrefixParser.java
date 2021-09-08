package PARSER;

import java.io.*;

import LEXER.*;

public class PrefixParser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public PrefixParser(Lexer l, BufferedReader br) {
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

    public void prog() {
        if (look.tag == '(') {
            stat();
            match(Tag.EOF);
        } else
            error("syntax error in prog method");
    }

    private void stat() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                statp();
                match(Token.rpt.tag);
                break;
            default:
                error("syntax error in stat method");
        }
    }

    private void statp() {
        switch (look.tag) {
            case '=':
                match(Token.assign.tag);
                match(Tag.ID);
                expr();
                break;
            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
                break;
            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
                break;
            default:
                error("syntax error in statp method");
        }
    }

    private void statlist() {
        switch (look.tag) {
            case '(':
                stat();
                statlistp();
                break;
            default:
                error("syntax error in statlist method");
        }
    }

    private void statlistp() {
        switch (look.tag) {
            case '(':
                stat();
                statlistp();
                break;
            case ')':
                break;
            default:
                error("syntax error in statlistp method");
        }
    }

    private void elseopt() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                match(Tag.ELSE);
                stat();
                match(Token.rpt.tag);
                break;
            case ')':
                break;
            default:
                error("syntax error in elseopt method");
        }
    }

    private void bexpr() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                bexprp();
                match(Token.rpt.tag);
                break;
            default:
                error("syntax error in bexpr method");
        }
    }

    private void bexprp() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("syntax error in bexprp method");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                exprp();
                match(Token.rpt.tag);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("syntax error in expr method");
        }
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match(Token.plus.tag);
                exprlist();
                break;
            case '-':
                match(Token.minus.tag);
                expr();
                expr();
                break;
            case '*':
                match(Token.mult.tag);
                exprlist();
                break;
            case '/':
                match(Token.div.tag);
                expr();
                expr();
                break;
            default:
                error("syntax error in expr method");
        }
    }

    private void exprlist() {
        switch (look.tag) {
            case '(':
            case Tag.ID:
            case Tag.NUM:
                expr();
                exprlistp();
                break;
            default:
                error("syntax error in exprlist method");
        }
    }

    private void exprlistp() {
        switch (look.tag) {
            case '(':
            case Tag.ID:
            case Tag.NUM:
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error("syntax error in exprlist method");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "INPUT/"; // il percorso del file da leggere
        path += (args != null && args.length > 0 && args[0] != null) ? args[0] : "dummy.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            PrefixParser parser = new PrefixParser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*

prog -> stat
statlist -> stat statlistp
statlistp -> ''
statlistp -> stat statlistp
stat -> ( statp )
statp -> = id expr
statp -> cond bexpr stat elseopt
statp -> while bexpr stat
statp -> do statlist
statp -> print exprlist
statp -> read id
elseopt -> ( else stat )
elseopt -> ''
bexpr -> ( bexprp )
bexprp -> relop expr expr
expr -> num
expr -> id
expr -> ( exprp )
exprp -> + exprlist
exprp -> - expr expr
exprp -> * exprlist
exprp -> / expr expr
exprlist -> expr exprlistp
exprlistp -> expr exprlistp
exprlistp -> ''

 */