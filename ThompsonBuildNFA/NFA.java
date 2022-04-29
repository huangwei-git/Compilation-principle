package ThompsonBuildNFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
        toe = "B";
        head = "A";
    }

    public void addTransfer(Pair pair, String c){
        if(transfer.containsKey(pair)) transfer.get(pair).add(c);
        else transfer.put(new Pair(pair.getHead(),pair.getInput()),new TreeSet<>(){{add(c);}});
        vn.add(pair.getHead());
        vn.add(c);
        vt.add(pair.getInput());
    }

    //合并NFA
    public String merge(NFA nfa){
        String res = toe;
        int bias = SToV(toe);
        nfa.shift(bias);
        toe = nfa.toe;
        transfer.putAll(nfa.getTransfer());
        vn.addAll(nfa.vn);
        vt.addAll(nfa.vt);
        return res;
    }

    // 本的NFA的头部连接所传入NFA的头部
    public void connectHeadToHead(NFA nfa){
        addTransfer(new Pair(head,"ε"),nfa.head);
        vt.add("ε");
        ;
    }

    // 头部连接尾部，空转移
    public void connectHeadToToe(){
        addTransfer(new Pair(head,"ε"),toe);
        vt.add("ε");
        ;
    }

    // 本NFA的尾部连接所传入NFA的头部
    public void connectToeToHead(String toe,NFA nfa){
        String newToe = nfa.toe;
        addTransfer(new Pair(toe,"ε"),nfa.head);
        this.toe = newToe;
        ;
    }

    // 本NFA尾部连接本NFA头部
    public void connectToeToHead(){
        addTransfer(new Pair(toe,"ε"), head);
        vt.add("ε");
        ;
    }

    // 头部插入一个新状态，转移条件为epsilon
    public void addToHead(){
        shift(1);
        vn.add("A");
        addTransfer(new Pair("A","ε"),head );
        head = "A";
        vt.add("ε");
        ;
    }

    // 尾部插入一个新状态，转移条件为epsilon
    public void addToToe(){
        String oldToe = toe;
        toe = shift(toe,1);
        addTransfer(new Pair(oldToe,"ε"),toe);
        ;
    }

    //NFA所有状态移动一个偏置量bias
    private void shift(int bias){
        toe = shift(toe,bias);
        head = shift(head,bias);
        TreeMap<Pair,TreeSet<String>> newTransfer = new TreeMap<>();
        TreeSet<String> newVN = new TreeSet<>();
        for(Pair pair : transfer.keySet()){
            String newHead = shift(pair.getHead(),bias);
            Pair newPair = new Pair(newHead,pair.getInput());
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
        ;
    }

    //把字符串s偏移step
    private String shift(String s,int step){
        return VToS(SToV(s) + step);
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
            v--;
            char c = (char) (v % 26 + 'A');
            res.insert(0,c);
            v /= 26;
        }
        return res.toString();
    }

    // 递归获取状态s的epsilon闭包
    public void epsilon(String state, Set<String> currentStates){
        // 存储状态
        currentStates.add(state);
        Pair pair = new Pair(state,"ε");
        // 若存在该转移方程
        if(transfer.containsKey(pair)){
            Set<String> states = transfer.get(pair);
            // 遍历转移方程对应的状态集
            for(String nextState : states){
                // 对每个没求过epsilon闭包的状态都求递归求epsilon闭包
                if(!currentStates.contains(nextState)){
                    currentStates.add(nextState);
                    epsilon(nextState,currentStates);
                }
            }
        }
    }

    public void identification(String input){
        // 存储当前所在状态
        HashSet<String> currentStates = new HashSet<>();
        // 出发点的epsilon闭包为初始状态
        epsilon(head,currentStates);
        // 遍历输入的字符串中的字符
        for(char c : input.toCharArray()){
            // 若当前状态为空，则退出
            if(currentStates.isEmpty()) break;
            // 存储下一次的状态
            HashSet<String> nextStates = new HashSet<>();
            // 遍历当前所在状态
            for(Object o : currentStates.toArray()){
                // 取出一个可达状态
                String start = (String)o;
                // 将状态从集合中移除
                currentStates.remove(start);
                Pair pair = new Pair(start,"" + c);
                // 遍历当前状态输入字符c后能到达的状态，存入nextStates中
                if(transfer.containsKey(pair))
                    for(String toes : transfer.get(pair))
                        nextStates.add(toes);
            }
            currentStates.addAll(nextStates);
            // 获取集合的epsilon闭包
            for(Object o : currentStates.toArray()){
                if(o instanceof String)
                    epsilon((String)o,currentStates);
            }
        }
        if(currentStates.contains(toe)) System.out.println("YES");
        else System.out.println("NO");
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

    //markdown画NFA：
    public void graph(){
        StringBuilder output = new StringBuilder();
        output.append(">" + toString()+"\n");
        if(vn.size() > 20){
            output.append("```mermaid\ngraph TB\n\n");
        }else{
            output.append("```mermaid\ngraph LR\n\n");
        }
        TreeMap<Pair, TreeSet<String>> map = transfer;
        for(Map.Entry<Pair,TreeSet<String>> entry: map.entrySet()){
            String start = entry.getKey().getHead();
            String input = entry.getKey().getInput();
            for(String end : entry.getValue()){
                output.append(start + "-->|" + input + "|" + end + "\n");
            }
        }
        output.append("```");
        File file = new File("./src/resource/NFA.md");
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(output.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fr != null){
                try {
                    fr.flush();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(file.getAbsolutePath());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        TreeSet<String> orderVN = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return SToV(o1) - SToV(o2);
            }
        });
        orderVN.addAll(vn);
        res.append("K={" + getString(orderVN) + "};\nΣ={" + getString(vt) + "};\n");
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
        res.append(equation + "S=[" + head + "];\n" + "Z={" + toe + "}");


        return res.toString();
    }

}