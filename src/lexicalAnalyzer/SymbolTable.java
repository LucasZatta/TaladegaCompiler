package lexicalAnalyzer;

import java.util.HashSet;
import java.util.Set;

public class SymbolTable {
    private Set<String> symbols = new HashSet<>();

    public void registerSymbol(String symbolName) {
        symbols.add(symbolName);
    }

    public void print() {
        System.out.println("------ Symbol Table ------");
        for (var symbol : symbols) {
            System.out.println("------------ { " + symbol +" }");
        }
        System.out.println("--------------------------");
    }
}
