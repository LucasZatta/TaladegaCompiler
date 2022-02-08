package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class IfStatement implements Statement, StatementWithCondition {
    private final SxExpression condition;
    private final StatementList stmtList;
    private final StatementList stmtListElse;

    public IfStatement(SxExpression condition, StatementList stmList, StatementList stmtListElse) {
        this.condition = condition;
        this.stmtList = stmList;
        this.stmtListElse = stmtListElse;
    }

    @Override
    public SxExpression getCondition() {
        return condition;
    }
}
