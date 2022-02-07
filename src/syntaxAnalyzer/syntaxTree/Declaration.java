package syntaxAnalyzer.syntaxTree;

import lexicalAnalyzer.TokenType;
import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpressionType;

import java.util.List;

public class Declaration {
    private List<Identifier> IdentifierList;
    private SxExpressionType Type;

    public Declaration(List<Identifier> identifierList, SxExpressionType type) {
        IdentifierList = identifierList;
        Type = type;
    }
}
