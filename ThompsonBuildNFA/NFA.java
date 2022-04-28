package ThompsonBuildNFA;

import java.util.*;

public class NFA {
    private String head;
    private String toe;
    private TreeSet<String> vn = new TreeSet<>();
    private TreeSet<String> vt = new TreeSet<>();
    private TreeMap<Pair,TreeSet<String>> transfer = new TreeMap<>();

/*构造函数*/
    public NFA(String input){
        transfer.put(new Pair("A",input),new TreeSet<>(){{add("B");}});
        vn.add("A");
        vn.add("B");
        vt.add(input);
        updateState();
    }

    public void addTransfer(Pair pair, String c){
        if(transfer.containsKey(pair)) transfer.get(pair).add(c);
        else transfer.put(new Pair(pair.getHead(),pair.getInput()),new TreeSet<>(){{add(c);}});
        vn.add(pair.getHead());
        vn.add(c);
        vt.add(pair.getInput());
        updateState();
    }

    //合并NFA
    public String merge(NFA nfa){
        String res = toe;
        int bias = SToV(toe);
        nfa.shift(bias);
        transfer.putAll(nfa.getTransfer());
        vn.addAll(nfa.vn);
        vt.addAll(nfa.vt);
        toe = nfa.toe;
        return res;
    }

    // 本的NFA的头部连接所传入NFA的头部
    public void connectHeadToHead(NFA nfa){
        addTransfer(new Pair(head,"ε"),nfa.head);
        vt.add("ε");
    }

    // 本的NFA的头部连接所传入NFA的头部
    public void connectHeadToToe(){
        addTransfer(new Pair(head,"ε"),toe);
        vt.add("ε");
    }

    // 本NFA的尾部连接本NFA的头部
    public void connectToeToHead(String toe,NFA nfa){
        addTransfer(new Pair(toe,"ε"),nfa.head);
        vt.add("ε");
    }

    // 本NFA尾部连接本NFA头部
    public void connectToeToHead(){
        addTransfer(new Pair(toe,"ε"), head);
        vt.add("ε");
    }

    // 所传入NFA尾部的连接本NFA的尾部
    public void connectToeToToe(NFA nfa){
        addTransfer(new Pair(nfa.toe,"ε"), toe);
        vt.add("ε");
    }

    // 头部插入一个新状态，转移条件为epsilon
    public void addToHead(){
        shift(1);
        vn.add("A");
        addTransfer(new Pair("A","ε"),head );
        head = "A";
        vt.add("ε");
    }

    // 尾部插入一个新状态，转移条件为epsilon
    public void addToToe(){
        String newToe = shift(toe,1);
        addTransfer(new Pair(toe,"ε"),newToe);
        vn.add(newToe);
        vt.add("ε");
    }

    //NFA所有状态移动一个偏置量bias
    private void shift(int bias){
        TreeMap<Pair,TreeSet<String>> newTransfer = new TreeMap<>();
        TreeSet<String> newVN = new TreeSet<>();
        for(Pair pair : transfer.keySet()){
            String newHead = shift(pair.getHead(),bias);
            Pair newPair = new Pair(shift(pair.getHead(),bias),pair.getInput());
            newTransfer.put(newPair,new TreeSet<>());
            for(String oldToe : transfer.get(pair)){
                String newToe = shift(oldToe,bias);
                newTransfer.get(newPair).add(newToe);
                newVN.add(newHead);
                newVN.add(newToe);
            }
        }
        transfer = newTransfer;
        vn = newVN;
        head = vn.first();
        toe = vn.last();
    }


    //把字符串状态
    private String shift(String s,int step){
        return VToS(SToV(s) + step);
    }

    public void updateState(){
        head = vn.first();
        toe = vn.last();
    }

    private int SToV(String s){
        int len = s.length();
        int res = s.charAt(--len) - 'A' + 1;
        int power = 26;
        while(--len >= 0){
            res += power * (s.charAt(len) - 'A' + 1);
            power *= 26;
        }
        return res;
}

    private String VToS(int v){
        StringBuilder res = new StringBuilder();
        while(v != 0){
            char c = (char) ((v - 1) % 26 + 'A');
            res.insert(0,c);
            v /= 26;
        }
        return res.toString();
    }

    public String getHead(){
        return head;
    }

    public String getToe() {
        return toe;
    }

    public TreeMap<Pair, TreeSet<String>> getTransfer() {
        return transfer;
    }

    private String getString(Set<String> set){
        StringBuilder res = new StringBuilder();
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            res.append(iterator.next());
            if(iterator.hasNext()) res.append(",");
         }
        return res.toString();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("K={" + getString(vn) + "};\nΣ={" + getString(vt) + "};\n");
        StringBuilder equation = new StringBuilder();
        equation.append("F={\n");
        for (Map.Entry<Pair,TreeSet<String>> entry : transfer.entrySet()){
            Pair key = entry.getKey();
            String head = key.getHead();
            String input = key.getInput();
            equation.append("    δ(" + head + "," + input + ")=" + entry.getValue() + ",\n");
        }
        int len = equation.length();
        equation.replace(len - 1,len,"\n};\n");
        res.append(equation + head + ";\n" + "Z={" + toe + "}");


        return res.toString();
    }

    public static void main(String[] args) {
        NFA nfa1 = new NFA("T");
        nfa1.addTransfer(new Pair("A","0"),"B");
        nfa1.addTransfer(new Pair("B","1"),"C");
        nfa1.addTransfer(new Pair("A","2"),"D");
        nfa1.addTransfer(new Pair("D","ε"),"E");
        nfa1.addTransfer(new Pair("C","3"),"E");

        NFA nfa2 = new NFA("T-2");
        nfa2.addTransfer(new Pair("A","0-2"),"B");
        nfa2.addTransfer(new Pair("B","1-2"),"C");
        nfa2.addTransfer(new Pair("A","2-2"),"D");
        nfa2.addTransfer(new Pair("A","ε-2"),"E");
        nfa2.addTransfer(new Pair("A","3-2"),"F");

        nfa1.addToHead();
        nfa1.addToToe();
        nfa1.merge(nfa2);
        nfa1.connectHeadToHead(nfa2);
        nfa1.connectToeToToe(nfa2);

        TreeMap<Pair,TreeSet<String>> map = nfa1.getTransfer();
        for(Pair pair : map.keySet()){
            for(String t : map.get(pair))
                System.out.println(pair.getHead() + " " + pair.getInput()+ "->" + t);
        }
        System.out.println(nfa1);
    }

}