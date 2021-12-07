package taladegaCompiler;

import customExceptions.LexicalException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.SymbolTable;

public class Main {

    public static void main(String[] args) {
        var fileName = "input.txt";

        if (args.length > 0)
            fileName = args[0];

        try {
            var symbolTable = new SymbolTable();
            var lexicalAnalyzer = new LexicalAnalyzer(fileName, symbolTable);
            var token = lexicalAnalyzer.scan();
            while (token != null) {

                System.out.println(token);

                token = lexicalAnalyzer.scan();
            }

            symbolTable.print();
        } catch (LexicalException e) {
            System.err.println(e.getError());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

