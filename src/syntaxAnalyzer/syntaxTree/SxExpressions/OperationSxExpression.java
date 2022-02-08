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

    public SxExpression getFirstExpression() {
        return firstExpression;
    }

    public SxExpression getSecondExpression() {
        return secondExpression;
    }

    public Token getOperatorToken() {
        return operatorToken;
    }

    //    @Override
//    public SxExpressionType getResultType() throws Exception {
//        var lExpType = leftExpression.getResultType();
//        var rExpType = rightExpression.getResultType();
//
//        var opTokType = operatorToken.TokenType;
//
//        if (opTokType.belongs(TokenType.relopTokenTypes()))
//            return SxExpressionType.INT;
//
//        if(TokenType.OPERATOR_DIV.equals(opTokType))
//            return SxExpressionType.FLOAT;
//
//        if(lExpType.equals(rExpType))
//            return lExpType;
//
//        if(SxExpressionType.CHAR.equals(lExpType) || SxExpressionType.CHAR.equals(rExpType))
//            throw new TreeExpressionException("Operation between Char and Float is Invalid");
//
//        return SxExpressionType.FLOAT;
//    }
}
