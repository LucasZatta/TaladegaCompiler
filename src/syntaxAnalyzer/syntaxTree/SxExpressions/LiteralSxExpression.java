package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public class LiteralSxExpression implements SxExpression {
    private final Token token;

    public LiteralSxExpression(Token token) {
        this.token = token;
    }

    @Override
    public SxExpressionType getResultType() {
        return SxExpressionType.getType(this.token);
    }
}
