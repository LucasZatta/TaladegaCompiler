package customExceptions;

import lexicalAnalyzer.Token;

public class LexicalException extends CompilerException {
    private final int line;
    private final int column;
    private StringBuilder wordBuffer;
    private String message = null;

    public LexicalException(int line, int column, StringBuilder wordBuffer, String message) {
        this.line = line;
        this.column = column;
        this.wordBuffer = wordBuffer;
        this.message = message;
    }

    public LexicalException(int line, int column, StringBuilder wordBuffer) {
        this(line, column, wordBuffer, null);
    }

    public String getError() {
        var builder = new StringBuilder()
                .append("LEXICAL ERROR ==> {")
                .append(System.lineSeparator())
                .append("\t[Message]: ").append(message)
                .append(System.lineSeparator())
                .append("\t[Line]: ").append(line)
                .append(System.lineSeparator())
                .append("\t[Column]: ").append(column)
                .append(System.lineSeparator())
                .append("\t[Buffer]: \"").append(wordBuffer).append("\"")
                .append(System.lineSeparator())
                .append("}");

        return builder.toString();
    }
}
