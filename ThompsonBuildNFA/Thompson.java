package ThompsonBuildNFA;

public class Thompson {
    private NFA nfa;
    private Node tree;

    public Thompson(){}

    public Thompson(String normalExpression){
        tree = new BuildTree().getTree(normalExpression);
    }

    public void BuildNFA(Node root){
        if(root != null){
           BuildNFA(root.rchild);
           BuildNFA(root.lchild);
        }
    }

    private void AND(NFA nfa1,NFA nfa2){
        nfa1.merge(nfa2);
    }

    private void OR(NFA nfa1,NFA nfa2){

    }
}
