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
}
