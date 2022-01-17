package syntaxAnalyzer;

import lexicalAnalyzer.Token;

public class FakeTokenWrapper {
    private Token token;
    private int fakeScope = 0;

    public Token getToken() {
        return token;
    }

    public int getFakeScope() {
        return fakeScope;
    }

    public FakeTokenWrapper(Token token, int fakeScope) {
        this.token = token;
        this.fakeScope = fakeScope;
    }
}
