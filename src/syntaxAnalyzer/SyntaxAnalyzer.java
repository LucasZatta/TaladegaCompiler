package syntaxAnalyzer;

import customExceptions.CompilerException;
import customExceptions.SyntaxException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;
import syntaxAnalyzer.syntaxTree.Declaration;
import syntaxAnalyzer.syntaxTree.DeclarationList;
import syntaxAnalyzer.syntaxTree.Identifier;
import syntaxAnalyzer.syntaxTree.Program;
import syntaxAnalyzer.syntaxTree.Statements.*;
import syntaxAnalyzer.syntaxTree.SxExpressions.SxExpressionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private Token eat(TokenType expectedTokenType) throws Exception {
        if (currentToken == null)
            throw new SyntaxException(0, 0, null,
                    String.format("Unexpected end of file. (Expected: '%s' )", expectedTokenType));

        if (expectedTokenType.equals(currentToken.TokenType)) {
            var lastEatenToken = currentToken;
            advance();
            return lastEatenToken;
        } else
            throw new SyntaxException(currentToken, expectedTokenType);

    }

    public void Analyze() throws Exception {
        program();
    }

    // ========================================================

    // ==> program identifier begin [decl-list] stmt-list end "." <==
    public Program program() throws Exception {
        eat(TokenType.KEYWORD_PROGRAM);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.KEYWORD_BEGIN);
        var declList = decl_list();
        var stmtList = stmt_list();
        eat(TokenType.KEYWORD_END);
        eat(TokenType.PUNCT_DOT);

        return new Program(stmtList, declList);
    }


    // ==> decl ";" { decl ";"} <==
    public DeclarationList decl_list() throws Exception {
        var declList = new DeclarationList();
        while (checkIfTokensAre_(this::decl)) {
            var decl = decl();
            declList.addElement(decl);
            eat(TokenType.PUNCT_SEMICOLON);
        }

        return declList;
    }

    // ==> ident-list is type <==
    public Declaration decl() throws Exception {
        var identList = ident_list();
        eat(TokenType.KEYWORD_IS);
        var type = type();
        return new Declaration(identList, type);
    }

    // ==> identifier {"," identifier} <==
    public List<Identifier> ident_list() throws Exception {
        var identifierList = new ArrayList<Identifier>();

        var ident = identifier();
        identifierList.add(ident);

        while (currentToken.TokenType.equals(TokenType.PUNCT_COLON)) {
            eat(TokenType.PUNCT_COLON);
            ident = identifier();
            identifierList.add(ident);
        }

        return identifierList;
    }

    // ==> identifier <==
    public Identifier identifier() throws Exception {
        var token = eat(TokenType.IDENTIFIER);
        return new Identifier(token);
    }

    // ==> int | float | char <==
    public SxExpressionType type() throws Exception {
        switch (currentToken.TokenType) {
            case TYPE_INT:
            case TYPE_FLOAT:
            case TYPE_CHAR:
                var token = eat(currentToken.TokenType);
                return SxExpressionType.getType(token);
            default:
                throw new SyntaxException(currentToken,
                        Arrays.asList(TokenType.TYPE_INT, TokenType.TYPE_FLOAT, TokenType.TYPE_CHAR));
        }
    }


    // ==> stmt { stmt } <==
    public StatementList stmt_list() throws Exception {
        var stmtList = new StatementList();

        var stmt = stmt();
        stmtList.addElement(stmt);

        while (checkIfTokensAre_stmt()) {
            stmt = stmt();
            stmtList.addElement(stmt);
        }

        return stmtList;
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
    public Statement stmt() throws Exception {

        // Creates an error expecting the end of the program case the current token is null
        if (currentToken == null)
            eat(TokenType.KEYWORD_END);

        Statement stmt;
        switch (currentToken.TokenType) {
            case IDENTIFIER: //Assign Statement
                stmt = assign_stmt();
                break;
            case KEYWORD_IF:
                stmt = if_stmt();
                break;
            case KEYWORD_WHILE:
                stmt = while_stmt();
                break;
            case KEYWORD_REPEAT:
                stmt = repeat_stmt();
                break;
            case KEYWORD_READ:
                stmt = read_stmt();
                break;
            case KEYWORD_WRITE:
                stmt = write_stmt();
                break;
            default:
                throw new SyntaxException(currentToken, "Invalid Statement Syntax");
        }
        eat(TokenType.PUNCT_SEMICOLON);

        return stmt;
    }


    // ==> identifier "=" simple_expr <==
    public AssignStatement assign_stmt() throws Exception {
        eat(TokenType.IDENTIFIER);
        eat(TokenType.ASSIGN);
        simple_expr();
    }


    // ==>  if condition then stmt-list end
    // | if condition then stmt-list else stmt-list end <==
    public IfStatement if_stmt() throws Exception {
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
                break;
            default:
                throw new SyntaxException(currentToken, Arrays.asList(TokenType.KEYWORD_END, TokenType.KEYWORD_ELSE));
        }
    }

    // ==> expression <==
    public void condition() throws Exception {
        expression();
    }


    // ==> repeat stmt-list stmt-suffix <==
    public RepeatStatement repeat_stmt() throws Exception {
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
    public WhileStatement while_stmt() throws Exception {
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
    public ReadStatement read_stmt() throws Exception {
        eat(TokenType.KEYWORD_READ);
        eat(TokenType.PUNCT_PARENTHESIS_OPEN);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
    }

    // ==> write "(" writable ")" <==
    public WriteStatement write_stmt() throws Exception {
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
            expression();
        }
    }

    // ==> term | simple-expr addop term <==
    public void simple_expr() throws Exception {
        term();
        if (checkIfTokensAre_(this::addop)) {
            addop();
            simple_expr();
        }
    }

    // ==> factor-a | term mulop factor-a <==
    public void term() throws Exception {
        factor_a();
        if (checkIfTokensAre_(this::mulop)) {
            mulop();
            term();
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
        var relopTokenTypes = TokenType.relopTokenTypes();

        if (currentToken.TokenType.belongs(relopTokenTypes))
            eat(currentToken.TokenType);
        else
            throw new SyntaxException(currentToken, relopTokenTypes);
    }

    // ==> "+" | "-" | || <==
    public void addop() throws Exception {
        var possibleTokenTypes = TokenType.addopTokenTypes();

        if (currentToken.TokenType.belongs(possibleTokenTypes))
            eat(currentToken.TokenType);
        else
            throw new SyntaxException(currentToken, possibleTokenTypes);
    }

    // ==> "*" | "/" | && <==
    public void mulop() throws Exception {
        var possibleTokenTypes = TokenType.mulopTokenTypes();

        if (currentToken.TokenType.belongs(possibleTokenTypes))
            eat(currentToken.TokenType);
        else
            throw new SyntaxException(currentToken, possibleTokenTypes);
    }

    // ==> integer_const | float_const | char_const <==
    public void constant() throws Exception {
        var possibleTokenTypes = TokenType.constantTokenTypes();

        if (currentToken.TokenType.belongs(possibleTokenTypes))
            eat(currentToken.TokenType);
        else
            throw new SyntaxException(currentToken, possibleTokenTypes);
    }
}
