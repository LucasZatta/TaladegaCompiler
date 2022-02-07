package syntaxAnalyzer.syntaxTree.SxExpressions;

import lexicalAnalyzer.Token;

public enum SxExpressionType {
    INT,
    FLOAT,
    CHAR;

    public static SxExpressionType getType(Token token) {
        switch (token.TokenType) {
            case TYPE_INT:
            case INTEGER_CONST:
                return SxExpressionType.INT;
            case TYPE_FLOAT:
            case FLOAT_CONST:
                return SxExpressionType.FLOAT;
            case TYPE_CHAR:
            case CHAR_CONST:
                return SxExpressionType.CHAR;
            default:
                throw new RuntimeException("Invalid Token Type");
        }
    }
}
