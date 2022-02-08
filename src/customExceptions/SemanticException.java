package customExceptions;

public class SemanticException extends CompilerException {
    private final int line;
    private final int column;

    public SemanticException(int line, int column, String message) {
        this.line = line;
        this.column = column;
        this.message = message;
    }

    public String getError() {
        var builder = new StringBuilder()
                .append("SEMANTIC ERROR ==> {")
                .append(System.lineSeparator())
                .append("\t[Message]: ").append(message)
                .append(System.lineSeparator())
                .append("\t[Line]: ").append(line)
                .append(System.lineSeparator())
                .append("\t[Column]: ").append(column)
                .append(System.lineSeparator())
                .append("}");

        return builder.toString();
    }
}
