package syntaxAnalyzer.syntaxTree;

import lexicalAnalyzer.TokenType;

import java.util.List;

public class Declaration {
    private List<Identifier> IdentifierList;
    private TokenType Type;

    public Declaration(List<Identifier> identifierList, TokenType type) {
        IdentifierList = identifierList;
        Type = type;
    }

}
