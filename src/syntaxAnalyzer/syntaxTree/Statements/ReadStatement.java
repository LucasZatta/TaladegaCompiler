package syntaxAnalyzer.syntaxTree.Statements;

import syntaxAnalyzer.syntaxTree.Identifier;

public class ReadStatement implements Statement {
    private final Identifier ident;

    public ReadStatement(Identifier ident) {
        this.ident = ident;
    }
}
