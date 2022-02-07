package syntaxAnalyzer.syntaxTree;

import syntaxAnalyzer.syntaxTree.Statements.StatementList;

public class Program {
    private StatementList stmList;
    private DeclarationList declList;

    public Program(StatementList stm, DeclarationList list) {
        this.stmList = stm;
        this.declList = list;
    }

    public StatementList getStmList() {
        return stmList;
    }

    public DeclarationList getDeclList() {
        return declList;
    }
}
