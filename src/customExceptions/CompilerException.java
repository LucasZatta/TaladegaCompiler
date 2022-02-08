package customExceptions;

public abstract class CompilerException extends Exception {
    protected String message = null;

    @Override
    public String getMessage() {
        return message;
    }

    public abstract String getError();
}
