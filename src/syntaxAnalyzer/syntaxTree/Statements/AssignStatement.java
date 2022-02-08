package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.Identifier;
import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpression;

public class AssignStatement implements Statement, StatementWithValueExpression {
    private final Identifier ident;
    private final SxExpression expression;

    public AssignStatement(Identifier ident, SxExpression exp) {
        this.ident = ident;
        this.expression = exp;
    }

    public Identifier getIdent() {
        return ident;
    }

    @Override
    public SxExpression getValueExpression() {
        return expression;
    }
}
