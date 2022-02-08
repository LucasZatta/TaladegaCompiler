package syntaxAnalyzer.syntaxTree;

import syntaxAnalyzer.syntaxTree.Statements.Statement;

import java.util.ArrayList;
import java.util.List;

public class SxTree {
    private Program root = null;
    private List<Statement> statements = new ArrayList<>();
    private List<Identifier> identifiers = new ArrayList<>();
    private List<Declaration> declarations = new ArrayList<>();

    public Program getRoot() {
        return root;
    }

    public void setRoot(Program root) {
        this.root = root;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

}
