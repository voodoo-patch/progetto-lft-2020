//package TRANSLATOR;
//
//import LEXER.Lexer;
//import LEXER.Tag;
//import LEXER.Token;
//import LEXER.Word;
//import TRANSLATOR.Instruction.OpCode;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//
//public class Translator_bak {
//    private Lexer lex;
//    private BufferedReader pbr;
//    private Token look;
//    SymbolTable st = new SymbolTable();
//    CodeGenerator code = new CodeGenerator();
//    int count = 0;
//
//    public Translator_bak(Lexer l, BufferedReader br) {
//        lex = l;
//        pbr = br;
//        move();
//    }
//
//    void move() {
//        look = lex.lexical_scan(pbr);
//        System.out.println("token = " + look);
//    }
//
//    void error(String s) {
//        throw new Error("near line " + lex.line + ": " + s);
//    }
//
//    void match(int t) {
//        if (look.tag == t) {
//            if (look.tag != Tag.EOF) move();
//        } else error("syntax error");
//    }
//
//    public void prog() {
//// ... completare ...
//        int lnext_prog = code.newLabel();
//        stat(lnext_prog);
//        code.emitLabel(lnext_prog);
//        match(Tag.EOF);
//        try {
//            code.toJasmin();
//        } catch (IOException e) {
//            System.out.println("IO error\n");
//        }
//        ;
//// ... completare ...
//    }
//
//    public void statp(int lnext) {
//        switch (look.tag) {
//// ... completare ...
//            case Tag.READ:
//                match(Tag.READ);
//                if (look.tag == Tag.ID) {
//                    int read_id_addr = st.lookupAddress(((Word) look).lexeme);
//                    if (read_id_addr == -1) {
//                        read_id_addr = count;
//                        st.insert(((Word) look).lexeme, count++);
//                    }
//                    match(Tag.ID);
//                    code.emit(OpCode.invokestatic, 0);
//                    code.emit(OpCode.istore, read_id_addr);
//                } else
//                    error("Error in grammar (stat) after read with " + look);
//                break;
//// ... completare ...
//        }
//    }
//
//        private void exprp() {
//            switch(look.tag) {
//// ... completare ...
//                case '-':
//                    match('-');
//                    expr();
//                    expr();
//                    code.emit(OpCode.isub);
//                    break;
//// ...
//            }
//        }
//}
