package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class WriteStatement implements Statement, StatementWithValueExpression {
    private final SxExpression writable;

    public WriteStatement(SxExpression writable) {
        this.writable = writable;
    }

    @Override
    public SxExpression getValueExpression() {
        return writable;
    }
}
