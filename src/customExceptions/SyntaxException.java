package customExceptions;

public class SyntaxException extends Exception{
    private final int line;
    private final int column;
    private String tokenValue;
    private String message = null;

    public SyntaxException(int line, int column, String tokenValue, String message) {
        this.line = line;
        this.column = column;
        this.tokenValue = tokenValue;
        this.message = message;
    }

    public SyntaxException(int line, int column, String tokenValue) {
        this(line, column, tokenValue, null);
    }

    public String getError() {
        var builder = new StringBuilder()
                .append("ERROR ==> {")
                .append(System.lineSeparator())
                .append("\t[Message]: ").append(message)
                .append(System.lineSeparator())
                .append("\t[Line]: ").append(line)
                .append(System.lineSeparator())
                .append("\t[Column]: ").append(column)
                .append(System.lineSeparator())
                .append("\t[Buffer]: \"").append(tokenValue).append("\"")
                .append(System.lineSeparator())
                .append("}");

        return builder.toString();
    }
}
