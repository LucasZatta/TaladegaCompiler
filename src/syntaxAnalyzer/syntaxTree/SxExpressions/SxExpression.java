package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.TokenType;

public interface SxExpression {
    public SxExpressionType getResultType() throws Exception;
}
