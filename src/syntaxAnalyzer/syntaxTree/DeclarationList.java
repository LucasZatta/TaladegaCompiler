package syntaxAnalyzer.syntaxTree;

import java.util.ArrayList;
import java.util.List;

public class DeclarationList {
    private List<Declaration> list;

    public DeclarationList() {
        list = new ArrayList<Declaration>();
    }

    public void addElement(Declaration decl) {
        list.add(decl);
    }

    public List<Declaration> getList() {
        return list;
    }
}
