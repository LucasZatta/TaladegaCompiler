package semanticAnalyzer;

import lexicalAnalyzer.Token;

public enum ValueType {
    INT,
    FLOAT,
    CHAR,
    CHAR_ARR;

    public static ValueType getType(Token token) {
        switch (token.TokenType) {
            case TYPE_INT:
            case INTEGER_CONST:
                return ValueType.INT;
            case TYPE_FLOAT:
            case FLOAT_CONST:
                return ValueType.FLOAT;
            case TYPE_CHAR:
            case CHAR_CONST:
                return ValueType.CHAR;
            case LITERAL:
                return ValueType.CHAR_ARR;
            default:
                throw new RuntimeException("Invalid Token Type");
        }
    }
}
