package ThompsonBuildNFA;

import java.util.*;

public class NFA {
    private String start;
    private String end;
    private TreeSet<String> vn = new TreeSet<>();
    private TreeSet<String> vt = new TreeSet<>();
    private TreeMap<Pair,String> transfer = new TreeMap<>();

/*构造函数*/
    public NFA(String input){
        transfer.put(new Pair("A",input),"B");
        vn.add("A");
        vn.add("B");
        vt.add(input);
        updateState();
    }

    public void addTransfer(Pair pair, String c){
        transfer.put(new Pair(pair.getStart(),pair.getInput()),c);
        vn.add(pair.getStart());
        vn.add(c);
        vt.add(pair.getInput());
        updateState();
    }

    //合并NFA
    public void merge(NFA nfa){
        int bias = SToV(end);
        System.out.println(bias);
        nfa.shift(bias);
        transfer.putAll(nfa.getTransfer());
        vn.addAll(nfa.vn);
        vt.addAll(nfa.vt);
        end = nfa.end;
    }

    public void connectHeadToToe(NFA nfa){
        addTransfer(new Pair(end,"epsilon"),nfa.start);
    }

    public void connextHeadToHead(NFA nfa){
        addTransfer(new Pair(start,"epsilon"),nfa.start);
    }

    public void addToHead(){
        shift(1);
        vn.add("A");
        addTransfer(new Pair("A","ε"),start);
        start = "A";
        vt.add("ε");
    }

    public void addToTail(){
        String newEnd = shift(end,1);
        addTransfer(new Pair(end,"ε"),newEnd);
        end = newEnd;
        vn.add(end);
        vt.add("ε");
    }

    //NFA所有状态移动一个偏置量bias
    private void shift(int bias){
        TreeMap<Pair,String> newTransfer = new TreeMap<>();
        TreeSet<String> newVN = new TreeSet<>();
        for(Pair pair : transfer.keySet()){
            String newStart = shift(pair.getStart(),bias);
            String newEnd = shift(transfer.get(pair),bias);
            newTransfer.put(new Pair(newStart,pair.getInput()),newEnd);
            newVN.add(newStart);
            newVN.add(newEnd);
        }
        transfer = newTransfer;
        vn = newVN;
        start = vn.first();
        end = vn.last();
    }


    //把字符串状态
    private String shift(String s,int step){
        return VToS(SToV(s) + step);
    }

    public void updateState(){
        start = vn.first();
        end = vn.last();
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

    public String getEnd() {
        return end;
    }

    public TreeMap<Pair, String> getTransfer() {
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
        res.append("K={" + getString(vn) + "}; Σ={" + getString(vt) + "}; ");
        StringBuilder equation = new StringBuilder();
        for (Map.Entry<Pair,String> entry : transfer.entrySet()){
            Pair key = entry.getKey();
            equation.append("f(" + key.getStart() + "," + key.getInput() + ")=" + entry.getValue() + ",");
        }
        int len = equation.length();
        equation.replace(len - 1,len,";");
        res.append(equation + " A;" + " Z={" + end + "}");


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

        nfa1.merge(nfa2);

        TreeMap<Pair,String> map = nfa1.getTransfer();
        for(Pair pair : map.keySet()){
            System.out.println(pair.getStart() + " " + pair.getInput()+ "->" + map.get(pair));
        }
        System.out.println(nfa1);
    }

}