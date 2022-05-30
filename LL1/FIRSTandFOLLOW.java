package LL1;

import Grammar.Grammar;

import java.io.File;
import java.util.*;

public class FIRSTandFOLLOW {
<<<<<<< HEAD
    private Grammar grammar;//五元组
    private Map<String,Set<String>> map;//用于求FOLLOW集，value为包含key的产生式集合
    private Map<String, Set<String>> firstSet;
    private Map<String, Set<String>> followSet;

    public FIRSTandFOLLOW(Grammar grammar){
        this.grammar = grammar;
        map = new HashMap<>();
        firstSet = new HashMap<>();
        followSet = new HashMap<>();
        for(String s:this.grammar.getVN()){
=======
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
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
            map.put(s,new HashSet<>());
        }
        getFirstAndFollow();
    }

    private Set<String> getFirstSet(String left){
        Set set = new HashSet();
<<<<<<< HEAD
        if(!firstSet.containsKey(left)){
            firstSet.put(left,new HashSet<>());
            for(String right: grammar.getProduction().get(left)){
                //预处理map集合，方便求FOLLOW
                for(char c : right.toCharArray()){
                    if(c >= 'A' && c <= 'Z'){
                        map.get("" + c).add(left + right);
                    }
                }
                // 遍历每个句型的每个字符
                int len = right.length();
                for(int i = 0;i < len;i++){
                    String s = right.substring(i,i + 1);
                    i++;
                    boolean isEpsilon = false;
                    // 1. 如果X -> a，则a加入F（X）
                    if(grammar.getVT().contains(s)){
                        if(s.equals("ε") && i < len) isEpsilon = true;
                        else{
                            set.add(s);
                            break;
                        }
                    }
                    // 2. 如果X -> A，则F（A）加入F（X）
                    else{
                        if(firstSet.containsKey(s)) set.addAll(firstSet.get(s));
                        else set.addAll(getFirstSet(s));
                        if(set.contains("ε") && i < len) isEpsilon = true;
                    }
                    // 当遇到空集时
                    while(isEpsilon) {
                        s = right.substring(i,i + 1);
                        i++;
                        if(firstSet.containsKey(s)) set.addAll(firstSet.get(s));
                        else set.addAll(getFirstSet(s));
                        // 如果没有epsilon或者后面妹有元素，则退出循环
                        if(set.contains("ε") && i < len) set.remove("ε");
                        else break;
                    }
                }

            }
            firstSet.get(left).addAll(set);
        }
        return firstSet.get(left);
=======
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
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
    }

    private Set getFollowSet(String left){
        Set set = new HashSet();
        if(!followSet.containsKey(left)){
            followSet.put(left,new HashSet<>());
<<<<<<< HEAD
            if(left.equals(grammar.getStart())) set.add("#");
            for(String production : map.get(left)){
                int len = production.length();
                char leftSYM = left.charAt(0);
                int i = 0;
                while(production.charAt(++i) != leftSYM);
                // 如果后面没有字符了
                if(++i == len){
                    set.addAll(getFollowSet(production.substring(0,1)));
=======
            if(left.equals(grammar.getStart())) followSet.get(left).add("#");
            for(List<String> list: map.get(left)){
                int len = list.size();
                int i = 0;
                while(!list.get(++i).equals(left));
                // 如果后面没有字符了
                if(++i == len){
                    Set<String> tmpSet = getFollowSet(list.get(0));
                    set.addAll(tmpSet);
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
                }
                // 如果后面还有字符
                else{
                    do{
<<<<<<< HEAD
                        String next = production.substring(i,i + 1);
=======
                        String next = list.get(i);
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
                        // 如果后面的字符是终结符
                        if(grammar.getVT().contains(next)){
                            set.add(next);
                            break;
                        }
                        // 如果后面的字符非终结符
                        else{
<<<<<<< HEAD
                            set.addAll(firstSet.get(next));
=======
                            Set<String> tmpSet = new HashSet<>(firstSet.get(next));
                            set.addAll(tmpSet);
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
                            if(set.contains("ε") && i + 1 < len) set.remove("ε");
                            else break;
                        }
                    }while(++i < len);
                    // 如果后面的字符都包含epsilon，则add firstSet( 产生式左端 )
                    if(set.contains("ε")){
                        set.remove("ε");
<<<<<<< HEAD
                        set.addAll(getFollowSet(production.substring(0,1)));
=======
                        Set<String> tmpSet = getFollowSet(list.get(0));
                        set.addAll(tmpSet);
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
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

<<<<<<< HEAD
    //获取某个字符的FIRST集合
    public Set getFirst(String s){
        return firstSet.get(s);
    }

    //获取某个字符的FOLLOW集合
    public Set getFollow(String s){
        return followSet.get(s);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(grammar.toString());
        res.append("/n");
        res.append("FIRST:\n");
        for(String left : firstSet.keySet()){
            res.append(left + ": " + firstSet.get(left));
            res.append("\n");
        }
        res.append("FOLLOW:\n");
        for(String left : followSet.keySet()){
            res.append(left + ": " + followSet.get(left));
            res.append("\n");
        }
        return res.toString();
    }

    public Map<String, Set<String>> getFirstSet() {
        return firstSet;
    }

    public Map<String, Set<String>> getFollowSet() {
        return followSet;
=======

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
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
    }
}
