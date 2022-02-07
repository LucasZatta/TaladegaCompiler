package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.Identifier;
import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class AssignStatement implements Statement {
    private final Identifier ident;
    private final SxExpression expression;

    public AssignStatement(Identifier ident, SxExpression exp) {
        this.ident = ident;
        this.expression = exp;
    }

    public Identifier getIdentifier() {
        return ident;
    }

    public SxExpression getExpression() {
        return expression;
    }
}
