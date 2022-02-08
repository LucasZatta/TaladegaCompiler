package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public interface StatementWithCondition extends Statement {
    public SxExpression getCondition();
}
