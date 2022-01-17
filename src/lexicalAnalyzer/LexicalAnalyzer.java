package lexicalAnalyzer;

import customExceptions.LexicalException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {

    private final FileReader fileReader;

    private int currentTokenNumber = 0;
    private int currentLine = -1;
    private int currentColumn = -1; // TODO: Check if it will be implemented

    private int currentReading = 0;
    private boolean readNextChar = true;

    private final SymbolTable symbolTable;

    public LexicalAnalyzer(String fileName, SymbolTable symbolTable) throws IOException {
        this.symbolTable = symbolTable;

        var file = new File(fileName);
        this.fileReader = new FileReader(file);

        currentTokenNumber = 0;
        currentLine = 1;
        currentColumn = 0;
    }

    public Token scan() throws Exception {
        currentTokenNumber++;
        var token = new Token();

        var wordBuffer = new StringBuilder();
        int state = 0;

        token.TokenNumber = currentTokenNumber;
        token.LineStart = currentLine;
        token.ColumnStart = currentColumn;

        while (currentReading != -1) {
            if (readNextChar) {
                currentReading = this.fileReader.read();
                if (currentReading == -1 && state != 20) break;
                currentColumn++;
            } else {
                readNextChar = true;
            }

            var ch = (char) currentReading;

            if (ch == '\n') {
                currentLine++;
                currentColumn = 0;
            }

            // Initial state
            if (state == 0) {
                if (Character.isWhitespace(ch))
                    continue;

                token.LineStart = currentLine;
                token.ColumnStart = currentColumn;

                wordBuffer.append(ch);

                switch (ch) {
                    case '{': // literal
                        state = 1;
                        continue;
                    case '*': // mult_operator
                        token.TokenType = TokenType.OPERATOR_MUL;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '/': // DIV_OPERATOR or Comment initial state
                        state = 2;
                        continue;
                    case '+': // plus_sign
                        token.TokenType = TokenType.OPERATOR_PLUS;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '-': // minus_sign
                        token.TokenType = TokenType.OPERATOR_MINUS;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '&': // &&
                        state = 3;
                        continue;
                    case '|': // ||
                        state = 4;
                        continue;
                    case '=': //  Assignment Or "=="
                        state = 5;
                        continue;
                    case '>': // Greater Than Or ">="
                        state = 6;
                        continue;
                    case '<': // Less Than Or "<="
                        state = 7;
                        continue;
                    case '!': // NOT or NOT EQUAL
                        state = 8;
                        continue;
                    case '(':
                        token.TokenType = TokenType.PUNCT_PARENTHESIS_OPEN;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ')':
                        token.TokenType = TokenType.PUNCT_PARENTHESIS_CLOSE;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ';':
                        token.TokenType = TokenType.PUNCT_SEMICOLON;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ',':
                        token.TokenType = TokenType.PUNCT_COLON;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '.':
                        token.TokenType = TokenType.PUNCT_DOT;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '\'': // char_const
                        state = 12;
                        continue;
                    default:
                        if (Character.isDigit(ch)) {
                            state = 9;
                            continue;
                        }
                        if (Character.isLetter(ch)) {
                            state = 20;
                            continue;
                        }
                        throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid Character at beginning of token");
                }
            }


            switch (state) {

                case 1: // Literal reading state
                    wordBuffer.append(ch);

                    if (ch == '}') {
                        token.TokenType = TokenType.LITERAL;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    if (token.LineStart != currentLine)
                        throw new LexicalException(currentLine, currentColumn, wordBuffer, "Line Break inside of Literal");
                    continue;
                    // --------------------------------------------
                case 2: // DIV_OPERATOR or Comment initial state
                    if (ch == '*') {
                        wordBuffer = new StringBuilder();
                        state = 100;
                        continue;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_DIV;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 3: // && Operator reading state
                    wordBuffer.append(ch);
                    if (ch == '&') {
                        token.TokenType = TokenType.OPERATOR_AND;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid Token <" + wordBuffer + ">");

                    // --------------------------------------------
                case 4: // || Operator reading state
                    wordBuffer.append(ch);
                    if (ch == '|') {
                        token.TokenType = TokenType.OPERATOR_OR;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid Token <" + wordBuffer + ">");

                    // --------------------------------------------
                case 5: // ASSIGNMENT or EQUAL state
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_EQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.ASSIGN;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 6: // GREATER THAN or GREATER OR EQUAL THAN
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_GREQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_GR;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 7: // LESS THAN or LESS OR EQUAL THAN
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_LSEQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_LS;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 8: // NOT or NOT EQUAL
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_NEQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_EXCLAMATION;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 9: // integer_const reading state
                    if (Character.isDigit(ch)) {
                        wordBuffer.append(ch);
                        continue;
                    }

                    if (ch == '.') {
                        state = 10;
                        continue;
                    }

                    if (tokenConstantEndCharacter(ch)) {
                        readNextChar = false;

                        token.TokenType = TokenType.INTEGER_CONST;
                        token.Value = wordBuffer.toString().stripTrailing();
                        return token;
                    }

                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid integer constant");

                    // --------------------------------------------
                case 10: // float_const initial reading state
                    wordBuffer.append(ch);

                    if (Character.isDigit(ch)) {
                        state = 11;
                        continue;
                    }

                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid float constant");

                case 11: // float_const secondary reading state
                    wordBuffer.append(ch);

                    if (Character.isDigit(ch))
                        continue;

                    if (tokenConstantEndCharacter(ch)) {
                        readNextChar = false;

                        token.TokenType = TokenType.FLOAT_CONST;
                        token.Value = wordBuffer.toString().stripTrailing();
                        return token;
                    }

                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid float constant");

                    // --------------------------------------------
                case 12: // char_const initial reading state
                    wordBuffer.append(ch);
                    state = 13;
                    continue;

                    // --------------------------------------------
                case 13: // char_const secondary reading state
                    wordBuffer.append(ch);
                    if (ch == '\'') {
                        token.TokenType = TokenType.CHAR_CONST;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid char constant");

                    // --------------------------------------------
                case 20: // KEYWORD or IDENTIFIER reading state
                    if (Character.isLetterOrDigit(ch) || ch == '_') {
                        wordBuffer.append(ch);
                        continue;
                    }

                    token.Value = wordBuffer.toString().stripTrailing();

                    switch (token.Value) {
                        case "program":
                            token.TokenType = TokenType.KEYWORD_PROGRAM;
                            break;
                        case "begin":
                            token.TokenType = TokenType.KEYWORD_BEGIN;
                            break;
                        case "end":
                            token.TokenType = TokenType.KEYWORD_END;
                            break;
                        case "is":
                            token.TokenType = TokenType.KEYWORD_IS;
                            break;
                        case "int":
                            token.TokenType = TokenType.TYPE_INT;
                            break;
                        case "float":
                            token.TokenType = TokenType.TYPE_FLOAT;
                            break;
                        case "char":
                            token.TokenType = TokenType.TYPE_CHAR;
                            break;
                        case "if":
                            token.TokenType = TokenType.KEYWORD_IF;
                            break;
                        case "then":
                            token.TokenType = TokenType.KEYWORD_THEN;
                            break;
                        case "else":
                            token.TokenType = TokenType.KEYWORD_ELSE;
                            break;
                        case "repeat":
                            token.TokenType = TokenType.KEYWORD_REPEAT;
                            break;
                        case "until":
                            token.TokenType = TokenType.KEYWORD_UNTIL;
                            break;
                        case "while":
                            token.TokenType = TokenType.KEYWORD_WHILE;
                            break;
                        case "do":
                            token.TokenType = TokenType.KEYWORD_DO;
                            break;
                        case "read":
                            token.TokenType = TokenType.KEYWORD_READ;
                            break;
                        case "write":
                            token.TokenType = TokenType.KEYWORD_WRITE;
                            break;
                        default:
                            token.TokenType = TokenType.IDENTIFIER;
                            symbolTable.registerSymbol(token.Value);
                            break;
                    }

                    readNextChar = false;
                    return token;

                // --------------------------------------------
                case 100: // Comment ending '*' reading state
                    if (ch == '*')
                        state = 101;

                    continue;

                    // --------------------------------------------
                case 101: // Comment ending '/' reading state
                    if (ch == '/')
                        state = 0;
                    else
                        state = 100;

                    continue;

                    // --------------------------------------------
                default: // Invalid state
                    throw new LexicalException(currentLine, currentColumn, wordBuffer, "Invalid analyser State <" + state + ">");
            }
        }

        if (state == 0)
            return null;

        if (state == 100 || state == 101)
            throw new LexicalException(token.LineStart, token.ColumnStart, wordBuffer, "Unclosed comment");
        else
            throw new LexicalException(currentLine, currentColumn, wordBuffer, "Unexpected end of file");

    }

    private boolean tokenConstantEndCharacter(char ch) {
        return Character.isWhitespace(ch)
                || ch == ';'
                || ch == ')'
                || ch == '+'
                || ch == '-'
                || ch == '*'
                || ch == '/'
                || ch == '='
                || ch == '!'
                || ch == '&'
                || ch == '|'
                || ch == '>'
                || ch == '<';
    }
}
