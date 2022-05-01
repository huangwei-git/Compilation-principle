package ThompsonBuildNFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NFA {
    private String head;// 开始
    private String toe;// 结束
    // 状态转移方程
    private HashMap<Pair,HashSet<String>> transfer = new HashMap<>();
    private HashSet<String> VN = new HashSet<>();
    private HashSet<String> VT = new HashSet<>();
    private String normalExpression;// 对应正规式

/*构造函数*/
    public NFA(String input){
        transfer.put(new Pair("A",input),new HashSet<>(){{add("B");}});
        VN.add("A");
        VN.add("B");
        VT.add(input);
        toe = "B";
        head = "A";
    }

    public void addTransfer(Pair pair, String c){
        if(transfer.containsKey(pair)) transfer.get(pair).add(c);
        else transfer.put(new Pair(pair.getHead(),pair.getInput()),new HashSet<>(){{add(c);}});
        VN.add(pair.getHead());
        VN.add(c);
        VT.add(pair.getInput());
    }

    //合并NFA
    public String merge(NFA nfa){
        String res = toe;
        int bias = SToV(toe);
        nfa.shift(bias);
        toe = nfa.toe;
        transfer.putAll(nfa.getTransfer());
        VN.addAll(nfa.VN);
        VT.addAll(nfa.VT);
        return res;
    }

    //NFA所有状态移动一个偏置量bias
    public void shift(int bias){
        toe = shift(toe,bias);
        head = shift(head,bias);
        HashMap<Pair,HashSet<String>> newTransfer = new HashMap<>();
        HashSet<String> newVN = new HashSet<>();
        for(Pair pair : transfer.keySet()){
            String newHead = shift(pair.getHead(),bias);
            Pair newPair = new Pair(newHead,pair.getInput());
            newTransfer.put(newPair,new HashSet<>());
            for(String oldToe : transfer.get(pair)){
                String newToe = shift(oldToe,bias);
                newTransfer.get(newPair).add(newToe);
                newVN.add(newHead);
                newVN.add(newToe);
            }
        }
        transfer = newTransfer;
        VN = newVN;
        ;
    }

    //把字符串s偏移step
    public String shift(String s, int step){
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
        int res = input.length();
        int idx = -1;
        // 存储当前所在状态
        HashSet<String> currentStates = new HashSet<>();
        // 出发点的epsilon闭包为初始状态
        epsilon(head,currentStates);
        // 遍历输入的字符串中的字符
        for(char c : input.toCharArray()){
            // 若当前状态为空，则退出
            if(currentStates.isEmpty()){
                res = idx;
                break;
            }
            idx++;
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
        System.out.println("input:\"" + input + "\"");
        if(currentStates.contains(toe)) System.out.println("YES");
        else{
            if (res == input.length())
                System.out.println("NO,error at[" + idx +"]:\"" + input.substring(0,res) + "[___]\";");
            else
                System.out.println("NO,error at ["+ idx +"]:\"" + input.substring(0,res) +
                        "[" + input.substring(res) + "]\";");
        }
        System.out.println("------------------------------------");
    }

    public String getHead(){
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getToe() {
        return toe;
    }

    public void setToe(String toe) {
        this.toe = toe;
    }

    public HashSet<String> getVN() {
        return VN;
    }

    public HashSet<String> getVT() {
        return VT;
    }

    public String getNormalExpression() {
        return normalExpression;
    }

    public void setNormalExpression(String normalExpression) {
        this.normalExpression = normalExpression;
    }

    public HashMap<Pair, HashSet<String>> getTransfer() {
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
    public void getGraph(){
        StringBuilder output = new StringBuilder();
        output.append("## 正规式：" + normalExpression + "\n");
        output.append(">" + toString().replace("δ","&emsp;δ")+"\n");
        StringBuilder sb = new StringBuilder();
        if(VN.size() > 20){
            output.append("```mermaid\ngraph TB\n\n");
        }else{
            output.append("```mermaid\ngraph LR\n\n");
        }
        HashMap<Pair, HashSet<String>> map = transfer;
        for(Map.Entry<Pair,HashSet<String>> entry: map.entrySet()){
            String start = entry.getKey().getHead();
            String input = entry.getKey().getInput();
            for(String end : entry.getValue()){
                output.append(start + "-->|" + input + "|" + end + "\n");
            }
        }
        output.append("```");
        File file = new File("./src/ThompsonBuildNFA/resource/NFA.md");
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
        // 将非终结符排序存储至orderVN中
        TreeSet<String> orderVN = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return SToV(o1) - SToV(o2);
            }
        });
        orderVN.addAll(VN);
        // 将终结符排序存储至orderVT中
        TreeSet<String> orderVT = new TreeSet<>(VT);
        res.append("K={" + getString(orderVN) + "};\nΣ={" + getString(orderVT) + "};\n");
        // 存储状态转移方程
        StringBuilder equation = new StringBuilder();
        equation.append("F={\n");
        // 排序状态方程的Key值
        TreeSet<Pair> orderPair = new TreeSet<>(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                if(o1.getHead().equals(o2.getHead())) return o1.compareTo(o2);
                else return SToV(o1.getHead()) - SToV(o2.getHead());
            }
        });
        orderPair.addAll(transfer.keySet());
        // 遍历keys
        for(Pair pair : orderPair){
            String head = pair.getHead();
            String input = pair.getInput();
            TreeSet<String> toes = new TreeSet<>(transfer.get(pair));
            // 使用之前实现了comparator方法的HashSet对象，用于排序状态转移的结果
            orderVN.clear();
            orderVN.addAll(transfer.get(pair));
            equation.append("    δ(" + head + "," + input + ")=" + orderVN + ",\n");
        }
        // 去除最后多于的 ','
        int len = equation.length();
        equation.replace(len - 1,len,"\n};\n");
        res.append(equation + "S={" + head + "};\n" + "Z={" + toe + "}");


        return res.toString();
    }

}