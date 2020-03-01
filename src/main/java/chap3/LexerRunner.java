package chap3;

import Stone.CodeDialog;
import Stone.Lexer;
import Stone.ParseException;
import Stone.Token;

public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        for(Token t; (t = lexer.read())!= Token.EOF;) {
            System.out.println("=> "+t.getText());
        }
    }
}
