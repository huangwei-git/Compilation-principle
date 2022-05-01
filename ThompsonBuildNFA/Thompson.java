package ThompsonBuildNFA;

public class Thompson {
    private NFA nfa;
    private Node tree;

    public Thompson(){}

    public Thompson(String normalExpression){
        tree = new BuildTree().getTree(normalExpression);
        nfa = BuildNFA(tree);
        nfa.setNormalExpression(normalExpression);
    }

    public NFA BuildNFA(Node root){
        NFA res = null;
        if(root != null){
            NFA rightNFA = BuildNFA(root.rchild);
            NFA leftNFA = null;
            leftNFA = BuildNFA(root.lchild);
            if(rightNFA == null) res = new NFA(root.value);
            else{
                res = calc(root.value,rightNFA,leftNFA);
            }
        }
        return res;
    }

    private NFA calc(String operator,NFA rightNFA,NFA leftNFA){
        NFA res = null;
        if(operator.equals("|")){
            res = OR(rightNFA,leftNFA);
        }else if(operator.equals("·")){
            res = AND(rightNFA,leftNFA);
        }else if(operator.equals("*")){
            res = klinClosure(rightNFA,leftNFA);
        }else if(operator.equals("+")){
            res = positiveClosure(rightNFA,leftNFA);
        }
        return res;
    }

    private NFA AND(NFA rightNFA,NFA leftNFA){
        String res = rightNFA.merge(leftNFA);
        connectToeToHead(res,rightNFA,leftNFA);
        return rightNFA;
    }

    private NFA OR(NFA rightNFA,NFA leftNFA){
        addToHead(rightNFA);
        String recordToe = rightNFA.getToe();
        rightNFA.merge(leftNFA);
        addToToe(rightNFA);
        connectHeadToHead(rightNFA,leftNFA);
        rightNFA.addTransfer(new Pair(recordToe,"ε"),rightNFA.getToe());
        return rightNFA;
    }


    private NFA positiveClosure(NFA rightNFA,NFA leftNFA){
        connectToeToHead(rightNFA);
        addToHead(rightNFA);
        addToToe(rightNFA);
        return rightNFA;
    }

    private NFA klinClosure(NFA rightNFA,NFA leftNFA){
        positiveClosure(rightNFA,null);
        connectHeadToToe(rightNFA);
        return rightNFA;
    }

    public NFA getNfa() {
        return nfa;
    }


    // 本的NFA的头部连接所传入NFA的头部
    public void connectHeadToHead(NFA nfa1,NFA nfa2){
        nfa1.addTransfer(new Pair(nfa1.getHead(),"ε"),nfa2.getHead());
        nfa1.getVT().add("ε");
        ;
    }

    // 头部连接尾部，空转移
    public void connectHeadToToe(NFA nfa){
        nfa.addTransfer(new Pair(nfa.getHead(),"ε"),nfa.getToe());
        nfa.getVT().add("ε");
        ;
    }

    // 本NFA的尾部连接所传入NFA的头部
    public void connectToeToHead(String toe,NFA nfa1,NFA nfa2){
        String newToe = nfa2.getToe();
        nfa1.addTransfer(new Pair(toe,"ε"),nfa2.getHead());
        nfa1.setToe(newToe);
    }

    // 本NFA尾部连接本NFA头部
    public void connectToeToHead(NFA nfa){
        nfa.addTransfer(new Pair(nfa.getToe(),"ε"), nfa.getHead());
        nfa.getVT().add("ε");
    }

    // 头部插入一个新状态，转移条件为epsilon
    public void addToHead(NFA nfa){
        nfa.shift(1);
        nfa.getVN().add("A");
        nfa.addTransfer(new Pair("A","ε"),nfa.getHead());
        nfa.setHead("A");
        nfa.getVT().add("ε");
        ;
    }

    // 尾部插入一个新状态，转移条件为epsilon
    public void addToToe(NFA nfa){
        String oldToe = nfa.getToe();
        nfa.setToe(nfa.shift(nfa.getToe(),1));
        nfa.addTransfer(new Pair(oldToe,"ε"),nfa.getToe());
        ;
    }

}
