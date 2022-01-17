package syntaxAnalyzer;

import customExceptions.CompilerException;
import customExceptions.SyntaxException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;

import java.util.Arrays;
import java.util.Stack;


public class SyntaxAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private final Stack<Token> tokenBuffer = new Stack<>();

    private boolean fakeMovement = false;
    private int currentFakeScope = 0;
    private final Stack<FakeTokenWrapper> fakeTokenBuffer = new Stack<>();

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws Exception {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentToken = lexicalAnalyzer.scan();
    }

    private Token getNextToken() throws Exception {
        if (tokenBuffer.isEmpty())
            return lexicalAnalyzer.scan();
        else
            return tokenBuffer.pop();
    }

    private void advance() throws Exception {
        currentToken = getNextToken();

        if (fakeMovement)
            fakeTokenBuffer.push(new FakeTokenWrapper(currentToken, currentFakeScope));
    }

    // Initializes a fake reading state where any token read can be reversed
    private int fake_startFakeMovement() throws Exception {
        if (fakeMovement)
            currentFakeScope++;
        else
            fakeMovement = true;

        fakeTokenBuffer.push(new FakeTokenWrapper(currentToken, currentFakeScope));

        return currentFakeScope;
    }

    // Stops fake movement of the specified scope
    private void fake_stopFakeMovement(int fakeScope) throws Exception {
        if (!fakeTokenBuffer.isEmpty()) {
            var fakeToken = fakeTokenBuffer.peek();

            while (!fakeTokenBuffer.isEmpty() && fakeToken.getFakeScope() >= fakeScope) {
                tokenBuffer.push(fakeToken.getToken());
                fakeTokenBuffer.pop();

                if (!fakeTokenBuffer.isEmpty()) fakeToken = fakeTokenBuffer.peek();
            }
        }

        currentToken = getNextToken();

        currentFakeScope = fakeScope;

        if (fakeScope == 0)
            fakeMovement = false;
    }

    // Check if next tokens(including current) match specified function. (Throws internal exception if needed)
    private boolean checkIfTokensAre_(VoidThrowingFunction function, boolean throwException) throws Exception {
        var fakeScope = fake_startFakeMovement();
        try {
            function.run();
            return true;
        } catch (CompilerException exception) {
            if (throwException)
                throw exception;
            return false;
        } finally {
            fake_stopFakeMovement(fakeScope);
        }
    }

    private boolean checkIfTokensAre_(VoidThrowingFunction function) throws Exception {
        return checkIfTokensAre_(function, false);
    }

    //Slide 27 AnaliseSintatica-Parte1
    private void eat(TokenType expectedTokenType) throws Exception {
        if (expectedTokenType.equals(currentToken.TokenType))
            advance();
        else
            throw new SyntaxException(currentToken, expectedTokenType);
    }

    public void Analyze() throws Exception {
        program();
    }

    // ========================================================

    // ==> program identifier begin [decl-list] stmt-list end "." <==
    public void program() throws Exception {
        eat(TokenType.KEYWORD_PROGRAM);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.KEYWORD_BEGIN);
        decl_list();
        stmt_list();
        eat(TokenType.KEYWORD_END);
        eat(TokenType.PUNCT_DOT);
    }


    // ==> decl ";" { decl ";"} <==
    public void decl_list() throws Exception {
        while (checkIfTokensAre_(this::decl)) {
            decl();
            eat(TokenType.PUNCT_SEMICOLON);
        }
    }

    // ==> ident-list is type <==
    public void decl() throws Exception {
        ident_list();
        eat(TokenType.KEYWORD_IS);
        type();
    }

    // ==> identifier {"," identifier} <==
    public void ident_list() throws Exception {
        eat(TokenType.IDENTIFIER);

        while (currentToken.TokenType.equals(TokenType.PUNCT_COLON)) {
            eat(TokenType.PUNCT_COLON);
            eat(TokenType.IDENTIFIER);
        }
    }

    // ==> int | float | char <==
    @SuppressWarnings("DuplicatedCode")
    public void type() throws Exception {
        switch (currentToken.TokenType) {
            case TYPE_INT:
            case TYPE_FLOAT:
            case TYPE_CHAR:
                eat(currentToken.TokenType);
                break;
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(TokenType.TYPE_INT, TokenType.TYPE_FLOAT, TokenType.TYPE_CHAR));
        }
    }


    // ==> stmt { stmt } <==
    public void stmt_list() throws Exception {
        stmt();

        while (checkIfTokensAre_stmt()) {
            stmt();
        }
    }

    // ==> { stmt } <==
    public boolean checkIfTokensAre_stmt() throws Exception {
        try {
            return checkIfTokensAre_(this::stmt, true);
        } catch (SyntaxException exception) {
            return !"Invalid Statement Syntax".equals(exception.getMessage());
        } catch (CompilerException exception) {
            return false;
        }
    }

    // ==> assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt  ";" <==
    public void stmt() throws Exception {
        switch (currentToken.TokenType) {
            case IDENTIFIER: //Assign Statement
                assign_stmt();
                break;
            case KEYWORD_IF:
                if_stmt();
                break;
            case KEYWORD_WHILE:
                while_stmt();
                break;
            case KEYWORD_REPEAT:
                repeat_stmt();
                break;
            case KEYWORD_READ:
                read_stmt();
                break;
            case KEYWORD_WRITE:
                write_stmt();
                break;
            default:
                throw new SyntaxException(currentToken, "Invalid Statement Syntax");
        }
        eat(TokenType.PUNCT_SEMICOLON);
    }


    // ==> identifier "=" simple_expr <==
    public void assign_stmt() throws Exception {
        eat(TokenType.IDENTIFIER);
        eat(TokenType.ASSIGN);
        simple_expr();
    }


    // ==>  if condition then stmt-list end
    // | if condition then stmt-list else stmt-list end <==
    public void if_stmt() throws Exception {
        eat(TokenType.KEYWORD_IF);
        condition();
        eat(TokenType.KEYWORD_THEN);
        stmt_list();
        switch (currentToken.TokenType) {
            case KEYWORD_END:
                eat(TokenType.KEYWORD_END);
                break;
            case KEYWORD_ELSE:
                eat(TokenType.KEYWORD_ELSE);
                stmt_list();
                eat(TokenType.KEYWORD_END);
            default:
                throw new SyntaxException(currentToken, Arrays.asList(TokenType.KEYWORD_END, TokenType.KEYWORD_ELSE));
        }
    }

    // ==> expression <==
    public void condition() throws Exception {
        expression();
    }


    // ==> repeat stmt-list stmt-suffix <==
    public void repeat_stmt() throws Exception {
        eat(TokenType.KEYWORD_REPEAT);
        stmt_list();
        stmt_suffix();
    }

    // ==> until conditionx <==
    public void stmt_suffix() throws Exception {
        eat(TokenType.KEYWORD_UNTIL);
        condition();
    }

    // ==> stmt-prefix stmt-list end <==
    public void while_stmt() throws Exception {
        stmt_prefix();
        stmt_list();
        eat(TokenType.KEYWORD_END);
    }

    // ==> while condition do <==
    public void stmt_prefix() throws Exception {
        eat(TokenType.KEYWORD_WHILE);
        condition();
        eat(TokenType.KEYWORD_DO);
    }


    // ==> read "(" identifier ")" <==
    public void read_stmt() throws Exception {
        eat(TokenType.KEYWORD_READ);
        eat(TokenType.PUNCT_PARENTHESIS_OPEN);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
    }

    // ==> write "(" writable ")" <==
    public void write_stmt() throws Exception {
        eat(TokenType.KEYWORD_WRITE);
        eat(TokenType.PUNCT_PARENTHESIS_OPEN);
        writable();
        eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
    }

    // ==> simple-expr | literal <==
    public void writable() throws Exception {
        if (currentToken.TokenType.equals(TokenType.LITERAL))
            eat(TokenType.LITERAL);
        else
            simple_expr();
    }


    // ==> simple-expr | simple-expr relop simple-expr <==
    public void expression() throws Exception {
        simple_expr();
        if (checkIfTokensAre_(this::relop)) {
            relop();
            simple_expr();
        }
    }

    // ==> term | simple-expr addop term <==
    public void simple_expr() throws Exception {
        term();
        if (checkIfTokensAre_(this::addop)) {
            addop();
            term();
        }
    }

    // ==> factor-a | term mulop factor-a <==
    public void term() throws Exception {
        factor_a();
        if (checkIfTokensAre_(this::mulop)) {
            mulop();
            factor_a();
        }
    }

    // ==> factor | ! factor | "-" factor <==
    public void factor_a() throws Exception {
        switch (currentToken.TokenType) {
            case OPERATOR_EXCLAMATION:
            case OPERATOR_MINUS:
                eat(currentToken.TokenType);
                break;
            default:
                break;
        }
        factor();
    }

    // ==> identifier | constant | "(" expression ")" <==
    public void factor() throws Exception {
        if (checkIfTokensAre_(this::constant))
            constant();
        else if (TokenType.IDENTIFIER.equals(currentToken.TokenType))
            eat(TokenType.IDENTIFIER);
        else if (TokenType.PUNCT_PARENTHESIS_OPEN.equals(currentToken.TokenType)) {
            eat(TokenType.PUNCT_PARENTHESIS_OPEN);
            expression();
            eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
        }
    }

    // ==> "==" | ">" | ">=" | "<" | "<=" | "!=" <==
    public void relop() throws Exception {
        switch (currentToken.TokenType) {
            case OPERATOR_EQ:
            case OPERATOR_GR:
            case OPERATOR_GREQ:
            case OPERATOR_LS:
            case OPERATOR_LSEQ:
            case OPERATOR_NEQ:
                eat(currentToken.TokenType);
                break;
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(
                                TokenType.OPERATOR_EQ,
                                TokenType.OPERATOR_GR,
                                TokenType.OPERATOR_GREQ,
                                TokenType.OPERATOR_LS,
                                TokenType.OPERATOR_LSEQ,
                                TokenType.OPERATOR_NEQ));
        }
    }

    // ==> "+" | "-" | || <==
    @SuppressWarnings("DuplicatedCode")
    public void addop() throws Exception {
        switch (currentToken.TokenType) {
            case OPERATOR_PLUS:
            case OPERATOR_MINUS:
            case OPERATOR_OR:
                eat(currentToken.TokenType);
                break;
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(
                                TokenType.OPERATOR_PLUS,
                                TokenType.OPERATOR_MINUS,
                                TokenType.OPERATOR_OR));
        }

    }

    // ==> "*" | "/" | && <==
    @SuppressWarnings("DuplicatedCode")
    public void mulop() throws Exception {
        switch (currentToken.TokenType) {
            case OPERATOR_MUL:
            case OPERATOR_DIV:
            case OPERATOR_AND:
                eat(currentToken.TokenType);
                break;
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(
                                TokenType.OPERATOR_MUL,
                                TokenType.OPERATOR_DIV,
                                TokenType.OPERATOR_AND));
        }
    }

    // ==> integer_const | float_const | char_const <==
    @SuppressWarnings("DuplicatedCode")
    public void constant() throws Exception {
        switch (currentToken.TokenType) {
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
                eat(currentToken.TokenType);
                break;
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(
                                TokenType.INTEGER_CONST,
                                TokenType.FLOAT_CONST,
                                TokenType.CHAR_CONST));
        }
    }
}
