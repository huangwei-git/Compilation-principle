package ThompsonBuildNFA;

import java.io.*;
import java.util.*;

public class InputIdentification {

    // 递归获取状态s的epsilon闭包
    public static void epsilon(String state, NFA nfa, Set<String> currentStates){
        // 存储状态
        currentStates.add(state);
        Pair pair = new Pair(state,"ε");
        // 若存在该转移方程
        if(nfa.getTransfer().containsKey(pair)){
            Set<String> states = nfa.getTransfer().get(pair);
            // 遍历转移方程对应的状态集
            for(String nextState : states){
                // 对每个没求过epsilon闭包的状态都求递归求epsilon闭包
                if(!currentStates.contains(nextState)){
                    epsilon(nextState,nfa,currentStates);
                }
            }
        }
    }

    public static void identification(Thompson thompson,String input){
        // 存储当前所在状态
        HashSet<String> currentStates = new HashSet<>();
        NFA nfa = thompson.getNfa();
        // 出发点的epsilon闭包为初始状态
        epsilon(nfa.getHead(),nfa,currentStates);
        // 遍历输入的字符串中的字符
        for(char c : input.toCharArray()){
            // 若当前状态为空，则退出
            if(currentStates.isEmpty()) break;
            // 存储下一次的状态
            HashSet<String> nextStates = new HashSet<>(currentStates);
            // 遍历当前所在状态
            for(Object o : currentStates.toArray()){
                // 取出一个可达状态
                String start = (String)o;
                // 将状态从集合中移除
                currentStates.remove(start);
                Pair pair = new Pair(start,"" + c);
                // 遍历当前状态输入字符c后能到达的状态，存入nextStates中
                if(nfa.getTransfer().containsKey(pair))
                    for(String toes : nfa.getTransfer().get(pair))
                        nextStates.add(toes);
            }
            currentStates.addAll(nextStates);
            // 获取集合的epsilon闭包
            for(Object o : currentStates.toArray()){
                if(o instanceof String)
                    epsilon((String)o,nfa,currentStates);
            }
        }
        if(currentStates.contains(nfa.getToe())) System.out.println("YES");
        else System.out.println("NO");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
//        Thompson thompson =  new Thompson("Go(((Java)|(Python))MySQL)*((Huang)+|(Wei)+)");
        Thompson thompson =  new Thompson("ab*");
//        System.out.println(thompson.getNfa());
/*
        // No --> GoJavaMySQLJavaMySQL【?????】 --->缺少【(Huang)+】|【(Wei)+】
        thompson.getNfa().identification("GoJavaMySQLJavaMySQL");
        // YES
        thompson.getNfa().identification("GoJavaMySQLJavaMySQLHuangHuang");
        // NO ---> GoJavaMySQLJavaMySQL【HuangWei】  ---> 应该为或运算,Huang与Wei不能同时出现
        thompson.getNfa().identification("GoJavaMySQLJavaMySQLHuangWei");
        // YES
        thompson.getNfa().identification("GoJavaMySQLPythonMySQLWei");
        // NO ---> GoJavaMySQLPython【?????】HuangHuang ---> 缺少【MySQL】
        thompson.getNfa().identification("GoJavaMySQLPythonHuangHuang");
        // NO ---> GoJavaMySQL【????】MySQLHuangHuang ---> 缺少【Java】|【Python】
        thompson.getNfa().identification("GoJavaMySQLMySQLHuangHuang");
        // YES
        thompson.getNfa().identification("GoWei");
*/
        thompson.getNfa().graph();
        System.out.println(thompson.getNfa());
        in.close();
    }
}
