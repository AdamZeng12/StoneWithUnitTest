package Stone.ast;
import Stone.Lexer.Token;

public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token t) { super(t); }
    public int value() { return token().getNumber(); }
}