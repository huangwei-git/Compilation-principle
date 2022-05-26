package LL1;

import java.util.LinkedList;
import java.util.List;

public class GrammarTreeNode {
    public String value;
    public List<GrammarTreeNode> childs;
    public int id;

    public GrammarTreeNode(String value) {
        this.value = value;
        childs = new LinkedList<>();
    }

}
