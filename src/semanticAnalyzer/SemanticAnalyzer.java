package semanticAnalyzer;

import customExceptions.SemanticException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.TokenType;
import syntaxAnalyzer.syntaxTree.Statements.AssignStatement;
import syntaxAnalyzer.syntaxTree.Statements.StatementWithValueExpression;
import syntaxAnalyzer.syntaxTree.SxExpressions.*;
import syntaxAnalyzer.syntaxTree.SxTree;

import java.util.HashMap;

public class SemanticAnalyzer {
    private final SxTree syntaxTree;
    private final HashMap<String, ValueType> identifiers = new HashMap<>();

    public SemanticAnalyzer(SxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public void analyze() throws SemanticException {
        checkDeclarations();
        checkIdentifiers();
    }

    private void checkDeclarations() throws SemanticException {
        for (var declaration : syntaxTree.getDeclarations()) {
            var type = ValueType.getType(declaration.getTypeToken());

            for (var ident : declaration.getIdentifierList()) {
                if (identifiers.containsKey(ident.getIdentifierCode()))
                    throw new SemanticException(
                            ident.getToken().LineStart,
                            ident.getToken().ColumnStart,
                            "Identifier cannot be declared multiple times");

                identifiers.put(ident.getIdentifierCode(), type);
            }
        }
    }

    private void checkIdentifiers() throws SemanticException {
        for (var ident : syntaxTree.getIdentifiers()) {
            if (!identifiers.containsKey(ident.getIdentifierCode()))
                throw new SemanticException(
                        ident.getToken().LineStart,
                        ident.getToken().ColumnStart,
                        "Identifier '" + ident.getIdentifierCode() + "' was not declared");
        }
    }

    private void checkStatements() throws Exception {
        for (var stmt : syntaxTree.getStatements()) {
            if (stmt instanceof StatementWithValueExpression) {
                var valueExp = ((StatementWithValueExpression) stmt).getValueExpression();
                var expValueType = getValueType(valueExp);

                if (stmt instanceof AssignStatement) {
                    var ident = ((AssignStatement) stmt).getIdent();
                    var identType = identifiers.get(ident.getIdentifierCode());

                    if (!expValueType.equals(identType)
                            && !identType.equals(ValueType.FLOAT)
                            && !expValueType.equals(ValueType.INT))
                        throw new SemanticException(
                                ident.getToken().LineStart,
                                ident.getToken().ColumnStart,
                                "Cant assign type '" + expValueType +
                                        "' to identifier declared as '" + identType + "'.");
                }
            }
// TODO: Finish statements
        }
    }

    private ValueType getValueType(SxExpression exp) throws Exception {
        if (exp instanceof ConstantSxExpression)
            return ValueType.getType(((ConstantSxExpression) exp).getToken());

        if (exp instanceof IdentifierSxExpression) {
            var identifier = ((IdentifierSxExpression) exp).getIdentifier();
            return identifiers.get(identifier.getIdentifierCode());
        }

        if (exp instanceof OperationSxExpression) {
            var opExpression = (OperationSxExpression) exp;
            var firstExpType = getValueType(opExpression.getFirstExpression());
            var opToken = opExpression.getOperatorToken();

            if (exp instanceof SingleTermOperationSxExpression)
                return evaluate(firstExpType, opToken, null);

            var secondExpType = getValueType(opExpression.getSecondExpression());

            return evaluate(firstExpType, opToken, null);
        }

        throw new Exception("Invalid Syntax tree exception class");
    }

    private ValueType evaluate(ValueType firstExpType, Token opToken, ValueType secondExpType) throws SemanticException {
        if (secondExpType == null) {
            if (ValueType.CHAR.equals(firstExpType) || ValueType.CHAR_ARR.equals(firstExpType))
                throw new SemanticException(
                        opToken.LineStart,
                        opToken.ColumnStart,
                        "Cant use \"-\" or \"!\" with char");

            if (TokenType.OPERATOR_EXCLAMATION.equals(opToken.TokenType))
                return ValueType.INT;

            return firstExpType;
        }

        if (opToken.TokenType.belongs(TokenType.relopTokenTypes()))
            return ValueType.INT;

        if (TokenType.OPERATOR_DIV.equals(opToken.TokenType))
            return ValueType.FLOAT;

        if (firstExpType.equals(secondExpType))
            return firstExpType;

        if (ValueType.CHAR.equals(firstExpType) || ValueType.CHAR.equals(secondExpType))
            throw new SemanticException(
                    opToken.LineStart,
                    opToken.ColumnStart,
                    "Operation between Char and Float is Invalid");

        return ValueType.FLOAT;
    }
}
