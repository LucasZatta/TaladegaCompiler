package lexicalAnalyzer;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    // Types
    TYPE_INT, // int
    TYPE_FLOAT, // float
    TYPE_CHAR, // char
    // Constants
    INTEGER_CONST, // [0-9]*
    FLOAT_CONST, // [0-9]+\.[0-9]+
    CHAR_CONST, // '.*'
    LITERAL, // {[A-Za-z]*}
    // Identifiers
    IDENTIFIER,
    // Keywords
    KEYWORD_PROGRAM, // program
    KEYWORD_BEGIN, // begin
    KEYWORD_END, // end
    KEYWORD_IS, // is
    KEYWORD_IF, // if
    KEYWORD_THEN, // then
    KEYWORD_ELSE, // else
    KEYWORD_REPEAT, // repeat
    KEYWORD_UNTIL, // until
    KEYWORD_WHILE, // while
    KEYWORD_DO, // do
    KEYWORD_READ, // read
    KEYWORD_WRITE, // write
    // Assign Statements
    ASSIGN, // "="
    // Operators
    OPERATOR_EQ, // "=="
    OPERATOR_GR, // ">"
    OPERATOR_GREQ, // ">="
    OPERATOR_LS, // "<"
    OPERATOR_LSEQ, // "<="
    OPERATOR_NEQ, // "!="
    OPERATOR_EXCLAMATION, // "!"
    OPERATOR_PLUS, // "+"
    OPERATOR_MINUS, // "-"
    OPERATOR_MUL, // "*"
    OPERATOR_DIV, // "/"
    OPERATOR_AND, // "&&"
    OPERATOR_OR, // "||"
    // Punctuations
    PUNCT_SEMICOLON, // ";"
    PUNCT_COLON, // ","
    PUNCT_DOT, // "."
    PUNCT_PARENTHESIS_OPEN, // "("
    PUNCT_PARENTHESIS_CLOSE; // ")"

    public boolean belongs(List<TokenType> tokenTypes) {
        return tokenTypes.contains(this);
    }

    public static List<TokenType> relopTokenTypes() {
        return Arrays.asList(
                TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_GR,
                TokenType.OPERATOR_GREQ,
                TokenType.OPERATOR_LS,
                TokenType.OPERATOR_LSEQ,
                TokenType.OPERATOR_NEQ);
    }

    public static List<TokenType> addopTokenTypes() {
        return Arrays.asList(
                TokenType.OPERATOR_PLUS,
                TokenType.OPERATOR_MINUS,
                TokenType.OPERATOR_OR);
    }

    public static List<TokenType> mulopTokenTypes() {
        return Arrays.asList(
                TokenType.OPERATOR_MUL,
                TokenType.OPERATOR_DIV,
                TokenType.OPERATOR_AND);
    }

    public static List<TokenType> constantTokenTypes() {
        return Arrays.asList(
                TokenType.INTEGER_CONST,
                TokenType.FLOAT_CONST,
                TokenType.CHAR_CONST);
    }
}
