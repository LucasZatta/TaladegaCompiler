package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class RepeatStatement implements Statement, StatementWithCondition {
    private final SxExpression condition;
    private final StatementList stmtList;

    public RepeatStatement(SxExpression condition, StatementList stmtList) {
        this.condition = condition;
        this.stmtList = stmtList;
    }

    @Override
    public SxExpression getCondition() {
        return condition;
    }
}
