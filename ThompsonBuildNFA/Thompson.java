package ThompsonBuildNFA;

public class Thompson {
    private NFA nfa;
    private Node tree;

    public Thompson(){}

    public Thompson(String normalExpression){
        tree = new BuildTree().getTree(normalExpression);
        nfa = BuildNFA(tree);
    }

    public NFA BuildNFA(Node root){
        NFA res = null;
        if(root != null){
            NFA rightNFA = BuildNFA(root.rchild);
            NFA leftNFA = BuildNFA(root.lchild);
            if(rightNFA == null) res = new NFA(root.value);
            else res = calc(root.value,rightNFA,leftNFA);
        }
        return res;
    }

    private NFA calc(String operator,NFA nfa2,NFA nfa1){
        NFA res = null;
        if(operator.equals("|")){
            res = OR(nfa2,nfa1);
        }else if(operator.equals("·")){
            res = AND(nfa2,nfa1);
        }else if(operator.equals("*")){
            res = klinClosure(nfa2,nfa1);
        }else if(operator.equals("+")){
            res = positiveClosure(nfa2,nfa1);
        }
        return res;
    }

    private NFA AND(NFA nfa2,NFA nfa1){
        String res = nfa2.merge(nfa1);
        nfa2.connectToeToHead(res,nfa1);
        return nfa2;
    }

    private NFA OR(NFA nfa2,NFA nfa1){
        nfa2.addToHead();
        String recordToe = nfa2.getToe();
        nfa2.merge(nfa1);
        nfa2.addToToe();
        nfa2.connectHeadToHead(nfa1);
        nfa2.addTransfer(new Pair(recordToe,"ε"),nfa2.getToe());
        return nfa2;
    }


    private NFA positiveClosure(NFA nfa,NFA nfaNULL){
        nfa.connectToeToHead();
        nfa.addToHead();
        nfa.addToToe();
        return nfa;
    }

    private NFA klinClosure(NFA nfa,NFA nfaNULL){
        nfa = positiveClosure(nfa,null);
        nfa.connectHeadToToe();
        return nfa;
    }

    public NFA getNfa() {
        return nfa;
    }
}
