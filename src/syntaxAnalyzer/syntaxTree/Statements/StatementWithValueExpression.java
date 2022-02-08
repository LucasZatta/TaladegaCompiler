package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public interface StatementWithValueExpression extends Statement {
    public SxExpression getValueExpression();
}
