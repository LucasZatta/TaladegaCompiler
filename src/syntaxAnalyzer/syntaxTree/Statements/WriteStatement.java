package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class WriteStatement implements Statement {
    private final SxExpression writable;

    public WriteStatement(SxExpression writable) {
        this.writable = writable;
    }
}
