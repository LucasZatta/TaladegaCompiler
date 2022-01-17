package syntaxAnalyzer;

import customExceptions.SyntaxException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.SymbolTable;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;


public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private Token nextToken;

    public SyntaxAnalyzer(String fileName, SymbolTable symbolTable) throws Exception {
        this.lexicalAnalyzer = new LexicalAnalyzer(fileName, symbolTable);
        this.currentToken = lexicalAnalyzer.scan();
        this.nextToken = lexicalAnalyzer.scan();
    }

    public void advance() throws Exception{
        currentToken = nextToken;
        nextToken = lexicalAnalyzer.scan();
    }

    //Slide 27 AnaliseSintatica-Parte1
    public void eat(TokenType expectedTokenType) throws Exception{
        if (currentToken.TokenType.equals(expectedTokenType))
            advance();
        else
            throw new SyntaxException(currentToken.getLineStart(), currentToken.getColumnStart(), currentToken.Value, "Unexpected Token received");
    }

    //Estamos em duvida em casos opcionais como decl_list
    public void program () throws Exception{
        eat(TokenType.KEYWORD_PROGRAM);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.KEYWORD_BEGIN);
        decl_list();
        stmt_list();
        eat(TokenType.KEYWORD_END);
        eat(TokenType.PUNCT_DOT);
    }


    public void decl_list() throws Exception{
        decl();
        eat(TokenType.PUNCT_SEMICOLON);
        //***********Como implementar a repetição opcional ({decl ;})?
    }

    public void decl() throws Exception{
        ident_list();
        eat(TokenType.KEYWORD_IS);
        type();
    }


    public void ident_list() throws Exception{
        eat(TokenType.IDENTIFIER);
        //******Implementar Repetição Opcional ({, identifier})
    }

    public void type() throws Exception{
        switch (currentToken.TokenType){
            case TYPE_INT:
                eat(TokenType.TYPE_INT);
                break;
            case TYPE_FLOAT:
                eat(TokenType.TYPE_FLOAT);
                break;
            case TYPE_CHAR:
                eat(TokenType.TYPE_CHAR);
                break;
            default:
                throw new SyntaxException(currentToken.getLineStart(), currentToken.getColumnStart(), currentToken.Value, "Unexpected Token received");
        }
    }

    public void stmt_list() throws Exception{
        stmt();
        //******Implementar Repetição Opcional ({; stmt})
    }

    public void stmt() throws Exception{
        switch (currentToken.TokenType){
            case IDENTIFIER://Assign Statement
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
                throw new SyntaxException(currentToken.getLineStart(), currentToken.getColumnStart(), currentToken.Value, "Unexpected Token received");
        }
    }

    public void assign_stmt() throws Exception{
        eat(TokenType.IDENTIFIER);
        eat(TokenType.ASSIGN);
        simple_expr();
    }

    public void if_stmt() throws Exception{
        eat(TokenType.KEYWORD_IF);
        condition();
        eat(TokenType.KEYWORD_THEN);
        stmt_list();
        switch(currentToken.TokenType){
            case KEYWORD_END:
                eat(TokenType.KEYWORD_END);
                break;
            case KEYWORD_ELSE:
                eat(TokenType.KEYWORD_ELSE);
                stmt_list();
                eat(TokenType.KEYWORD_END);
            default:
                throw new SyntaxException(currentToken.getLineStart(), currentToken.getColumnStart(), currentToken.Value, "Unexpected Token received");
        }

    }

    public void condition() throws Exception{
        expression();
    }

    public void repeat_stmt() throws Exception{
        eat(TokenType.KEYWORD_REPEAT);
        stmt_list();
        stmt_suffix();
    }

    public void stmt_suffix() throws Exception{
        eat(TokenType.KEYWORD_UNTIL);
        condition();
    }

    public void while_stmt() throws Exception{
        stmt_prefix();
        stmt_list();
        eat(TokenType.KEYWORD_END);
    }

    public void stmt_prefix() throws Exception{
        eat(TokenType.KEYWORD_WHILE);
        condition();
        eat(TokenType.KEYWORD_DO);
    }

    public void read_stmt() throws Exception{
        eat(TokenType.KEYWORD_READ);
        eat(TokenType.PUNCT_PARENTHESIS_OPEN);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
    }

    public void write_stmt() throws Exception{
        eat(TokenType.KEYWORD_WRITE);
        eat(TokenType.PUNCT_PARENTHESIS_OPEN);
        writable();
        eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
    }

    public void writable() throws Exception{
        if(currentToken.TokenType.equals(TokenType.LITERAL))
            eat(TokenType.LITERAL);
        else
            simple_expr();
    }

    public void expression() throws Exception{
        simple_expr();
        if(relop(nextToken) != null){
            eat(relop(nextToken));
            simple_expr();
        }
        //Testar o tratamento de ambiguidade para o expression
    }

    public void simple_expr() throws Exception{
        term();
        if(addop(nextToken) != null){
            eat(addop(nextToken));
            term();
        }
    }

    public void term() throws Exception{
        factor_a();
        if(mulop(nextToken) != null){
            eat(mulop(nextToken));
            factor_a();
        }
    }

    public void factor_a() throws Exception{
        switch (currentToken.TokenType){
            case OPERATOR_EXCLAMATION:
                eat(TokenType.OPERATOR_EXCLAMATION);
            case OPERATOR_MINUS:
                eat(TokenType.OPERATOR_MINUS);
        }
        factor();
    }

    public void factor() throws Exception{
        if(constant(currentToken) != null)
            eat(constant(currentToken));
        else if (currentToken.TokenType.equals(TokenType.IDENTIFIER))
            eat(TokenType.IDENTIFIER);
        else if (currentToken.TokenType.equals(TokenType.PUNCT_PARENTHESIS_OPEN)){
            eat(TokenType.PUNCT_PARENTHESIS_OPEN);
            expression();
            eat(TokenType.PUNCT_PARENTHESIS_CLOSE);
        }
    }

    public TokenType relop(Token token) throws Exception{
        return switch (token.TokenType) {
            case OPERATOR_EQ -> TokenType.OPERATOR_EQ;
            case OPERATOR_GR -> TokenType.OPERATOR_GR;
            case OPERATOR_GREQ -> TokenType.OPERATOR_GREQ;
            case OPERATOR_LS -> TokenType.OPERATOR_LS;
            case OPERATOR_LSEQ -> TokenType.OPERATOR_LSEQ;
            case OPERATOR_NEQ -> TokenType.OPERATOR_NEQ;
            default -> null;
        };
    }

    public TokenType addop(Token token) throws Exception{
        return switch (token.TokenType) {
            case OPERATOR_PLUS -> TokenType.OPERATOR_PLUS;
            case OPERATOR_MINUS -> TokenType.OPERATOR_MINUS;
            case OPERATOR_OR -> TokenType.OPERATOR_OR;
            default -> null;
        };
    }

    public TokenType mulop(Token token) throws Exception{
        return switch (token.TokenType) {
            case OPERATOR_MUL -> TokenType.OPERATOR_MUL;
            case OPERATOR_DIV -> TokenType.OPERATOR_DIV;
            case OPERATOR_AND -> TokenType.OPERATOR_AND;
            default -> null;
        };
    }

    public TokenType constant(Token token) throws Exception{
        return switch (token.TokenType) {
            case INTEGER_CONST -> TokenType.INTEGER_CONST;
            case FLOAT_CONST -> TokenType.FLOAT_CONST;
            case CHAR_CONST -> TokenType.CHAR_CONST;
            default -> null;
        };
    }

}
