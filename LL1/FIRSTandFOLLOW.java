package LL1;

import Grammar.Grammar;

import java.io.File;
import java.util.*;

public class FIRSTandFOLLOW {
    Grammar grammar;
    Map<String,Set<List<String>>> map;
    Map<String, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public FIRSTandFOLLOW(File file){
        grammar = new Grammar(file);
        map = new HashMap<>();
        firstSet = new HashMap<>();
        followSet = new HashMap<>();
        for(String s:grammar.getVN()){
            map.put(s,new HashSet<>());
        }
        getFirstAndFollow();
    }

    private Set<String> getFirstSet(String left){
        Set set = new HashSet();
        for(List<String> list : grammar.getProduction().get(left)){
            // 为方便求FOLLOW，预处理map
            LinkedList tmp = new LinkedList(list);
            tmp.addFirst(left);
            for(String s:list) if(grammar.getVN().contains(s)) map.get(s).add(tmp);
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
                    if(firstSet.containsKey(s)) set.addAll(firstSet.get(s));
                    else set.addAll(getFirstSet(s));
                    if(set.contains("ε") && iterator.hasNext()) isEpsilon = true;
                }
                // 当遇到空集时
                while(isEpsilon) {
                    s = iterator.next();
                    if(firstSet.containsKey(s)) set.addAll(firstSet.get(s));
                    else set.addAll(getFirstSet(s));
                    // 如果没有epsilon或者后面妹有元素，则退出循环
                    if(set.contains("ε") && iterator.hasNext()) set.remove("ε");
                    else break;
                }
            }

        }
        firstSet.put(left,set);
        return set;
    }

    private Set getFollowSet(String left){
        Set set = new HashSet();
        if(!followSet.containsKey(left)){
            followSet.put(left,new HashSet<>());
            if(left.equals(grammar.getStart())) followSet.get(left).add("#");
            for(List<String> list: map.get(left)){
                int len = list.size();
                int i = 0;
                while(!list.get(++i).equals(left));
                // 如果后面没有字符了
                if(++i == len){
                    Set<String> tmpSet = getFollowSet(list.get(0));
                    set.addAll(tmpSet);
                }
                // 如果后面还有字符
                else{
                    do{
                        String next = list.get(i);
                        // 如果后面的字符是终结符
                        if(grammar.getVT().contains(next)){
                            set.add(next);
                            break;
                        }
                        // 如果后面的字符非终结符
                        else{
                            Set<String> tmpSet = new HashSet<>(firstSet.get(next));
                            set.addAll(tmpSet);
                            if(set.contains("ε") && i + 1 < len) set.remove("ε");
                            else break;
                        }
                    }while(++i < len);
                    // 如果后面的字符都包含epsilon，则add firstSet( 产生式左端 )
                    if(set.contains("ε")){
                        set.remove("ε");
                        Set<String> tmpSet = getFollowSet(list.get(0));
                        set.addAll(tmpSet);
                    }
                }
                followSet.get(left).addAll(set);
            }
        }else set = followSet.get(left);
        return set;
    }

    private void getFirstAndFollow(){
        //遍历非终结符,求FIRST集
        for(String left : grammar.getVN()){
            if(!firstSet.containsKey(left))
                getFirstSet(left);
        }
        //遍历非终结符,求FOLLOW集
        for(String left : grammar.getVN()){
            if(!followSet.containsKey(left))
                getFollowSet(left);
        }
    }


    public void print(){
        System.out.println(grammar);
        System.out.println("FIRST:");
        for(String left : firstSet.keySet()){
            System.out.println(left + ": " + firstSet.get(left));
        }
        System.out.println("FOLLOW:");
        for(String left : followSet.keySet()){
            System.out.println(left + ": " + followSet.get(left));
        }
    }
}
