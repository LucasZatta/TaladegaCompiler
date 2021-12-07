package lexicalAnalyzer;

public class Token {
    public TokenType TokenType;
    public String Value = "";

    public int LineStart = 0;
    // TODO: "Add LineEnd"
    // public Integer LineEnd = null;
    public int ColumnStart = 0;
    // TODO: "Add ColumnEnd"
    // public Integer ColumnEnd = null;

    @Override
    public String toString() {
        var builder = new StringBuilder()
                .append("Token")
                .append("[")
                .append(String.format("%2d", LineStart))
                .append(":")
                .append(String.format("%2d", ColumnStart))
                .append("]")
                .append(": ")
                .append("<").append(TokenType).append(", \"").append(Value)
                .append("\">");
        return builder.toString();
    }
}