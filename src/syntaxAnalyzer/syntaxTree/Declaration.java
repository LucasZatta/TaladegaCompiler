package syntaxAnalyzer.syntaxTree;

import lexicalAnalyzer.Token;

import java.util.List;

public class Declaration {
    private final List<Identifier> identifierList;
    private final Token typeToken;

    public Declaration(List<Identifier> identifierList, Token typeToken) {
        this.identifierList = identifierList;
        this.typeToken = typeToken;
    }

    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    public Token getTypeToken() {
        return typeToken;
    }
}
