package TRANSLATOR;

import LEXER.*;
import TRANSLATOR.Instruction.OpCode;

import java.io.*;

public class Translator {
    private static String input;
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        input = (args != null && args.length > 0 && args[0] != null) ? args[0] : "dummy.txt";
        String path = "INPUT/"; // il percorso del file da leggere
        path += input;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private int tryGetIdAddress() {
        int read_id_addr = st.lookupAddress(((Word) this.look).lexeme);
        if (read_id_addr == -1) {
            read_id_addr = this.count;
            st.insert(((Word) this.look).lexeme, this.count++);
        }
        return read_id_addr;
    }

    public void prog() {
        if (look.tag == '(') {
            int lnext_prog = code.newLabel();
            stat(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin(input);
            } catch (IOException e) {
                System.out.println("IO error\n");
            }
        } else
            error("syntax error in prog method");
    }

    private void stat(int lnext) {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                statp(lnext);
                match(Token.rpt.tag);
                break;
            default:
                error("syntax error in stat method");
        }
    }

    public void statp(int lnext) {
        switch (look.tag) {
            case '=':
                match(Token.assign.tag);
                if (look.tag == Tag.ID) {
                    int read_id_addr = this.tryGetIdAddress();
                    match(Tag.ID);
                    expr();
                    code.emit(OpCode.istore, read_id_addr);
                } else
                    error("Error in grammar (statp) after read with " + look);
                break;
            case Tag.COND:
                match(Tag.COND);
                int l_iftrue = code.newLabel();
                int l_else = code.newLabel();
                bexpr(l_iftrue, l_else, true);
                code.emitLabel(l_iftrue); //should be removable
                stat(lnext);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(l_else);
                elseopt(lnext);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                int l_whiletrue = code.newLabel();
                int l_whilecond = code.newLabel();
                code.emitLabel(l_whilecond);
                bexpr(l_whiletrue, lnext, true);
                code.emitLabel(l_whiletrue);
                stat(l_whilecond);
                code.emit(OpCode.GOto, l_whilecond);
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist(lnext);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist(OpCode.invokestatic, 1);
                //code.emit(OpCode.invokestatic, 1); // print
                break;
            case Tag.READ:
                match(Tag.READ);
                if (look.tag == Tag.ID) {
                    int read_id_addr = this.tryGetIdAddress();
                    match(Tag.ID);
                    code.emit(OpCode.invokestatic, 0); // read
                    code.emit(OpCode.istore, read_id_addr);
                } else
                    error("Error in grammar (statp) after read with " + look);
                break;
            default:
                error("syntax error in statp method");
        }
    }

    private void statlist(int lnext) {
        switch (look.tag) {
            case '(':
                int statlist_label = code.newLabel();
                stat(statlist_label);
                code.emitLabel(statlist_label);
                statlistp(lnext);
                break;
            default:
                error("syntax error in statlist method");
        }
    }

    private void statlistp(int lnext) {
        switch (look.tag) {
            case '(':
                int statlist_label = code.newLabel();
                stat(statlist_label);
                code.emitLabel(statlist_label);
                statlistp(lnext);
                break;
            case ')':
                break;
            default:
                error("syntax error in statlistp method");
        }
    }

    private void elseopt(int l_next) {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                match(Tag.ELSE);
                stat(l_next);
                match(Token.rpt.tag);
                break;
            case ')':
                break;
            default:
                error("syntax error in elseopt method");
        }
    }

    private void bexpr(int l_iftrue, int l_else, boolean reverse) {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                bexprp(l_iftrue, l_else, reverse);
                match(Token.rpt.tag);
                break;
            default:
                error("syntax error in bexpr method");
        }
    }

    /**
     * Added production for 5.2 optional exercise:
     * bexprp -> AND bexpr bexpr | OR bexpr bexpr | NOT bexpr
     * <p>
     * Switched RELOP conditional jump due to 5.3 optional exercise by using Instruction.getOppositeRelop
     * following this map:
     * if_icmpeq -> if_icmpne
     * if_icmpne -> if_icmpeq
     * if_icmpgt -> if_icmple
     * if_icmpge -> if_icmplt
     * if_icmplt -> if_icmpge
     * if_icmple -> if_icmpgt
     */
    private void bexprp(int l_iftrue, int l_else, boolean reverse) {
        if (Word.and.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.AND);
            int first_cond_label = code.newLabel();
            bexpr(first_cond_label, l_else, true);
            code.emitLabel(first_cond_label);
            bexpr(l_iftrue, l_else, true);
        } else if (Word.or.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.OR);
            int second_cond_label = code.newLabel();
            bexpr(l_iftrue, second_cond_label, false);
            code.emitLabel(second_cond_label);
            bexpr(l_iftrue, l_else, true);
        } else if (Word.not.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.NOT);
            bexpr(l_else, l_iftrue, false);
        } else if (Word.eq.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmpeq) : OpCode.if_icmpeq;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else if (Word.ne.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmpne) : OpCode.if_icmpne;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else if (Word.lt.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmplt) : OpCode.if_icmplt;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else if (Word.le.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmple) : OpCode.if_icmple;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else if (Word.gt.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmpgt) : OpCode.if_icmpgt;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else if (Word.ge.lexeme.equals(((Word) this.look).lexeme)) {
            match(Tag.RELOP);
            expr();
            expr();
            OpCode relop = reverse ? Instruction.getOppositeRelop(OpCode.if_icmpge) : OpCode.if_icmpge;
            int jumpLabel = reverse ? l_else : l_iftrue;
            code.emit(relop, jumpLabel);
        } else {
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
                int id_address = this.tryGetIdAddress();
                match(Tag.ID);
                code.emit(OpCode.iload, id_address); /* symbol */
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) this.look).GetNumber()); /* constant val */
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
                exprlist(OpCode.iadd, null);
                //code.emit(OpCode.iadd);
                break;
            case '-':
                match(Token.minus.tag);
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match(Token.mult.tag);
                exprlist(OpCode.imul, null);
                //code.emit(OpCode.imul);
                break;
            case '/':
                match(Token.div.tag);
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            default:
                error("syntax error in expr method");
        }
    }

    private void exprlist(OpCode operation, Integer param) {
        switch (look.tag) {
            case '(':
            case Tag.ID:
            case Tag.NUM:
                expr();
                exprlistp(operation, param, true);
                break;
            default:
                error("syntax error in exprlist method");
        }
    }

    private void exprlistp(OpCode operation, Integer param, boolean first) {
        switch (look.tag) {
            case '(':
            case Tag.ID:
            case Tag.NUM:
                if (first && operation == OpCode.invokestatic && param == 1)
                    code.emit(operation, param);
                expr();
                if (param != null)
                    code.emit(operation, param);
                else
                    code.emit(operation);
                exprlistp(operation, param, false);
                break;
            case ')':
                if (first) {
                    int neutral = Instruction.GetNeutral(operation);
                    if (neutral >= 0)
                        code.emit(OpCode.ldc, neutral);
                    if (param != null)
                        code.emit(operation, param);
                    else
                        code.emit(operation);
                }
                break;
            default:
                error("syntax error in exprlist method");
        }
    }
}
