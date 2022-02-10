package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public class SingleTermOperationSxExpression extends OperationSxExpression implements SxExpression {

    public SingleTermOperationSxExpression(SxExpression expression, Token operatorToken) {
        super(expression, operatorToken, null);
    }
}
