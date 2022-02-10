package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public class ConstantSxExpression implements SxExpression {
    private final Token token;

    public ConstantSxExpression(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int getLineStart() {
        return token.LineStart;
    }

    @Override
    public int getColumnStart() {
        return token.ColumnStart;
    }
}
