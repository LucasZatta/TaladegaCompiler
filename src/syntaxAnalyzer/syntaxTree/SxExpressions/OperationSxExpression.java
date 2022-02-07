package syntaxAnalyzer.syntaxTree.SxExpressions;

import customExceptions.TreeExpressionException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;

public class OperationSxExpression implements SxExpression {
    private final SxExpression leftExpression;
    private final SxExpression rightExpression;
    private final Token operatorToken;

    public OperationSxExpression(SxExpression leftExpression, SxExpression rightExpression, Token operatorToken) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operatorToken = operatorToken;
    }

    @Override
    public SxExpressionType getResultType() throws Exception {
        var lExpType = leftExpression.getResultType();
        var rExpType = rightExpression.getResultType();

        var opTokType = operatorToken.TokenType;

        if (opTokType.belongs(TokenType.relopTokenTypes()))
            return SxExpressionType.INT;

        if(TokenType.OPERATOR_DIV.equals(opTokType))
            return SxExpressionType.FLOAT;

        if(lExpType.equals(rExpType))
            return lExpType;

        if(SxExpressionType.CHAR.equals(lExpType) || SxExpressionType.CHAR.equals(rExpType))
            throw new TreeExpressionException("Operation between Char and Float is Invalid");

        return SxExpressionType.FLOAT;
    }
}
