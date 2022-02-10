package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class WhileStatement implements Statement, StatementWithValueExpression {
    private final SxExpression condition;
    private final StatementList stmtList;

    public WhileStatement(SxExpression condition, StatementList stmtList) {
        this.condition = condition;
        this.stmtList = stmtList;
    }

    @Override
    public SxExpression getValueExpression() {
        return condition;
    }
}
