package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public class OperationSxExpression implements SxExpression {
    protected final SxExpression firstExpression;
    protected final SxExpression secondExpression;
    protected final Token operatorToken;

    public OperationSxExpression(SxExpression firstExpression, Token operatorToken, SxExpression secondExpression) {
        this.firstExpression = firstExpression;
        this.operatorToken = operatorToken;
        this.secondExpression = secondExpression;
    }

    @Override
    public int getLineStart() {
        return firstExpression.getLineStart();
    }

    @Override
    public int getColumnStart() {
        return firstExpression.getColumnStart();
    }

    public SxExpression getFirstExpression() {
        return firstExpression;
    }

    public SxExpression getSecondExpression() {
        return secondExpression;
    }

    public Token getOperatorToken() {
        return operatorToken;
    }
}
