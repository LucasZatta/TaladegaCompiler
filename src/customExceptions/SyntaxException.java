package customExceptions;

import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;

import java.util.Collection;
import java.util.stream.Collectors;

public class SyntaxException extends CompilerException {
    private final int line;
    private final int column;
    private final Token token;

    public SyntaxException(int line, int column, Token token, String message) {
        this.line = line;
        this.column = column;
        this.token = token;
        this.message = message;
    }

    public SyntaxException(Token token, String message) {
        this(token.LineStart, token.ColumnStart, token, message);
    }

    public SyntaxException(Token token, Collection<TokenType> expectedTokenTypes) {
        this(token.LineStart,
                token.ColumnStart,
                token,
                String.format("Unexpected Token (Expected: '%s' | Received: '%s')",
                        "[" + expectedTokenTypes.stream()
                                .map(Enum::toString)
                                .collect(Collectors.joining(",")) + "]",
                        token.TokenType)
        );
    }

    public SyntaxException(Token token, TokenType expectedTokenType) {
        this(token.LineStart,
                token.ColumnStart,
                token,
                String.format("Unexpected Token (Expected: '%s' | Received: '%s')", expectedTokenType, token.TokenType)
        );
    }

    public String getError() {
        var builder = new StringBuilder()
                .append("SYNTAX ERROR ==> {")
                .append(System.lineSeparator())
                .append("\t[Message]: ").append(message)
                .append(System.lineSeparator())
                .append("\t[Line]: ").append(line)
                .append(System.lineSeparator())
                .append("\t[Column]: ").append(column)
                .append(System.lineSeparator())
                .append("\t[Token]: \"").append(token).append("\"")
                .append(System.lineSeparator())
                .append("}");

        return builder.toString();
    }
}
