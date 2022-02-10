package syntaxAnalyzer.syntaxTree.SxExpressions;

import syntaxAnalyzer.syntaxTree.Identifier;

public class IdentifierSxExpression implements SxExpression {
    private final Identifier identifier;

    public IdentifierSxExpression(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public int getLineStart() {
        return identifier.getToken().LineStart;
    }

    @Override
    public int getColumnStart() {
        return identifier.getToken().ColumnStart;
    }
}
