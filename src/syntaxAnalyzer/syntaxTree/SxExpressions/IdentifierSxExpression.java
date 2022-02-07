package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public class IdentifierSxExpression implements SxExpression {
    private final Token token;

    public IdentifierSxExpression(Token token) {
        this.token = token;
    }

    @Override
    public SxExpressionType getResultType() {
        return SxExpressionType.getType(this.token);
    }
}
