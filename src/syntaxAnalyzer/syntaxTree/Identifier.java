package syntaxAnalyzer.syntaxTree;

import lexicalAnalyzer.Token;

public class Identifier {
    private Token token;

    public Identifier(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String getIdentifierCode(){
        return token.Value;
    }
}
