package LL1;

import Grammar.Grammar;

import javax.imageio.ImageTranscoder;
import java.io.File;
import java.util.*;

public class FIRST {
    Grammar grammar;
    Map<String, Set<String>> F;

    public FIRST(File file){
        grammar = new Grammar(file);
        F = new HashMap<>();
        getFirstSet();
    }

    private Set<String> getFirstSet(String left){
        Set set = new HashSet();
        for(List<String> list : grammar.getProduction().get(left)){
            Iterator<String> iterator = list.iterator();
            // 遍历每个句型的每个字符
            if(iterator.hasNext()){
                String s = iterator.next();
                boolean isEpsilon = false;
                // 1. 如果X -> a，则a加入F（X）
                if(grammar.getVT().contains(s)){
                    if(s.equals("ε") && iterator.hasNext()) isEpsilon = true;
                    else set.add(s);
                }
                // 2. 如果X -> A，则F（A）加入F（X）
                else{
                    set.addAll(getFirstSet(s));
                    if(set.contains("ε") && iterator.hasNext()) isEpsilon = true;
                }
                // 当遇到空集时
                while(isEpsilon) {
                    s = iterator.next();
                    if(F.containsKey(s)) set.addAll(F.get(s));
                    else set.addAll(getFirstSet(s));
                    // 如果没有epsilon或者后面妹有元素，则退出循环
                    if(set.contains("ε") && iterator.hasNext()) set.remove("ε");
                    else break;
                }
            }
        }
        return set;
    }

    private void getFirstSet(){
        for(String element : grammar.getVT()) F.put(element,new HashSet<>(){{add(element);}});
        // 遍历所有非终结符的产生式
        for(String element : grammar.getVN()){
            HashSet<String> set = new HashSet<>();
            //遍历非终结符能生成的所有句型
            if(F.containsKey(element)) continue;
            F.put(element,getFirstSet(element));
        }
    }

    public void print(){
        System.out.println(grammar);
        for(String left : F.keySet()){
            if(grammar.getVT().contains(left)) continue;
            System.out.println(left + ": " + F.get(left));
        }
    }
}
