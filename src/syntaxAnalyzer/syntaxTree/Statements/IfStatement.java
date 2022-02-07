package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class IfStatement implements Statement {
    private final SxExpression condition;
    private final StatementList stmList;
    private final StatementList stmListElse;

    public IfStatement(SxExpression condition, StatementList stmList, StatementList stmListFalse) {
        this.condition = condition;
        this.stmList = stmList;
        this.stmListElse = stmListFalse;
    }
}
