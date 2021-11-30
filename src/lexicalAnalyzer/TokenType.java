package lexicalAnalyzer;

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
    ASSIGN,
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
    PUNCT_PARENTHESIS_CLOSE, // ")"
}
