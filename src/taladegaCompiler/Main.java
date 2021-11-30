package taladegaCompiler;

import lexicalAnalyzer.LexicalAnalyzer;


public class Main {
    public static final String textFile = "$a != $b";

    public static void main(String[] args) throws Exception {
        var fileName = "input.txt";

        if(args.length > 0)
            fileName = args[0];

        try {
            var token = LexicalAnalyzer.scan();
            while (token != null) {

                System.out.println("Token: < " + token.TokenType + ", " + token.identifier + " >");

                token =  LexicalAnalyzer.scan();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}

