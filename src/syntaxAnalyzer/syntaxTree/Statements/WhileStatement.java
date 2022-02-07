package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class WhileStatement implements Statement {
    private final SxExpression condition;
    private final StatementList stmList;

    public WhileStatement(SxExpression condition, StatementList stmList) {
        this.condition = condition;
        this.stmList = stmList;
    }
}
