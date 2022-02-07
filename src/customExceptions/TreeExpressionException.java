package customExceptions;

public class TreeExpressionException extends CompilerException {
    private String message = null;

    @Override
    public String getMessage() {
        return message;
    }

    public TreeExpressionException(String message) {
        this.message = message;
    }

    public String getError() {
        var builder = new StringBuilder()
                .append("EXPRESSION ERROR ==> {")
                .append(System.lineSeparator())
                .append("\t[Message]: ").append(message)
                .append(System.lineSeparator())
                .append("}");

        return builder.toString();
    }
}
