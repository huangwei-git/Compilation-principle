package ThompsonBuildNFA;

public class Thompson {

// 输入正规式，返回对应NFA
public static NFA buildNFA(String normalExpression){
    NFA nfa = buildNFA(InversePolish.getTree(normalExpression));
    nfa.setNormalExpression(normalExpression);
    return nfa;
}

// 后序遍历传入的二叉树，创建对应NFA
private static NFA buildNFA(Node root){
    NFA res = null;
    if(root != null){
        NFA rightNFA = buildNFA(root.getRightChild());
        NFA leftNFA = null;
        leftNFA = buildNFA(root.getLeftChild());
        if(rightNFA == null) res = new NFA(root.getValue());
        else{
            res = combineNFA(root.getValue(),rightNFA,leftNFA);
        }
    }
    return res;
}

// 根据操作符operator的类型，按照相应规则合并right和leftNFA
private static NFA combineNFA(String operator, NFA rightNFA, NFA leftNFA){
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

private static NFA AND(NFA rightNFA,NFA leftNFA){
    String res = rightNFA.merge(leftNFA);
    String newToe = leftNFA.getToe();
    rightNFA.addTransfer(new Pair(res,"ε"),leftNFA.getHead());
    rightNFA.setToe(newToe);
    return rightNFA;
}

private static NFA OR(NFA rightNFA,NFA leftNFA){
    rightNFA.shift(1);
    rightNFA.getVN().add("A");
    rightNFA.addTransfer(new Pair("A","ε"),rightNFA.getHead());
    rightNFA.setHead("A");
    rightNFA.getVT().add("ε");
    String recordToe = rightNFA.getToe();
    rightNFA.merge(leftNFA);
    String oldToe = rightNFA.getToe();
    rightNFA.setToe(rightNFA.shift(rightNFA.getToe(),1));
    rightNFA.addTransfer(new Pair(oldToe,"ε"),rightNFA.getToe());

    rightNFA.addTransfer(new Pair(rightNFA.getHead(),"ε"),leftNFA.getHead());
    rightNFA.getVT().add("ε");
    rightNFA.addTransfer(new Pair(recordToe,"ε"),rightNFA.getToe());
    return rightNFA;
}


private static NFA positiveClosure(NFA rightNFA,NFA leftNFA){
    rightNFA.addTransfer(new Pair(rightNFA.getToe(),"ε"), rightNFA.getHead());
    rightNFA.getVT().add("ε");

    rightNFA.shift(1);
    rightNFA.getVN().add("A");
    rightNFA.addTransfer(new Pair("A","ε"),rightNFA.getHead());
    rightNFA.setHead("A");
    rightNFA.getVT().add("ε");

    String oldToe = rightNFA.getToe();
    rightNFA.setToe(rightNFA.shift(rightNFA.getToe(),1));
    rightNFA.addTransfer(new Pair(oldToe,"ε"),rightNFA.getToe());
    return rightNFA;
}

private static NFA klinClosure(NFA rightNFA,NFA leftNFA){
    positiveClosure(rightNFA,null);
    rightNFA.addTransfer(new Pair(rightNFA.getHead(),"ε"),rightNFA.getToe());
    rightNFA.getVT().add("ε");
    return rightNFA;
}

}
