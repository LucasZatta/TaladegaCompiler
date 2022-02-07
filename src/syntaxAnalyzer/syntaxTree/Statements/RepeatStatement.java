package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class RepeatStatement implements Statement {
    private final SxExpression condition;
    private final StatementList stmList;

    public RepeatStatement(SxExpression condition, StatementList stmList) {
        this.condition = condition;
        this.stmList = stmList;
    }
}
