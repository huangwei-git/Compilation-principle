package GrammarCategory;

import java.io.*;
import java.util.*;

public class Production {

    // 非终结符
    private HashSet<String> VN = new HashSet<>(){{
        add("A");add("B");add("C");add("D");add("E");add("F");add("G");add("H");add("I");add("J");add("K");add("L");add("M");
        add("N");add("O");add("P");add("Q");add("R");add("S");add("T");add("U");add("V");add("W");add("X");add("Y");add("Z");
    }};
    // 终结符
    private HashSet<String> VT = new HashSet<>(){{
        add("a");add("b");add("c");add("d");add("e");add("f");add("g");add("h");add("i");add("j");add("k");add("l");add("m");
        add("n");add("o");add("p");add("q");add("r");add("s");add("t");add("u");add("v");add("w");add("x");add("y");add("z");
    }};
    private String epsilon = "ε";

    // 加载文件，并调用write()方法将文件内容分解，写入productions中
    public int work(String filePath,Map<String,Collection<String>> productions) {
        File file = new File(filePath);
        int res = -1;
        try {
            load(new BufferedReader(new InputStreamReader(new FileInputStream(filePath))),productions);
            res = getGrammarType(productions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProductionException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void load(BufferedReader Content,Map<String, Collection<String>> productions) {
        try {
            StringBuilder line = new StringBuilder();
            String epsilon = "ε";
            while (!line.append(Content.readLine()).toString().equals("null")) {
                int len = line.length();
                if (len > 0) {
                    // 去除空格
                    line.replace(0, len, line.toString().replaceAll(" ", ""));
                    int indexOfSymbol = line.indexOf("->");
                    String left = line.substring(0, indexOfSymbol);
                    if (!productions.containsKey(left))// 左边存入产生式集合中
                        productions.put(left, new HashSet<>());
                    String[] rights = line.delete(0, indexOfSymbol + 2).toString().split("\\|");// 右边
                    for (String right : rights) {
                        // 删除右边的epsilon，除单个epsilon外
                        if (!right.equals(epsilon)) right = right.replaceAll(epsilon, " ");
                        productions.get(left).add(right);
                    }
                    line.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getGrammarType(Map<String,Collection<String>> productions) throws ProductionException{
        int res = -1;
        TreeSet<Integer> types = new TreeSet<>();
        for (String left : productions.keySet()){
            for (String right : productions.get(left)){
                int type = getAProduncionGrammarType(left,right);
                if(type == -2) throw new ProductionException(-2);
                if(type >= 0) types.add(type);
                System.out.println(left+"->"+right+" == "+type);
            }
        }
        // 异常
        if(types.size() == 0) throw new ProductionException(-1);

        if((res = types.pollFirst()) >= 3){
            if(res == 3 && types.size() == 2) res = 2;
            else if(res == 4 && types.size() == 1) res = 2;
            else res = 3;
        }
        return res;
    }

    // 获得一个产生式的文法类型
    public int getAProduncionGrammarType(String left,String right){
        int type = -2;
        if(isNA(left,right)) type = GrammarType.NA;
        else if(isRG(left,right)){
            if (isLRG(left,right)) type = GrammarType.LRG;
            else if(isRRG(left,right)) type = GrammarType.RRG;
            else type = GrammarType.RG;
        }
        else if(isCFG(left,right)) type = GrammarType.CFG;
        else if(isCSG(left, right)) type = GrammarType.CSG;
        else if(isPSG(left, right)) type = GrammarType.PSG;
        return type;
    }

    public boolean isNA(String left,String right){
        boolean res = false;
        if(right.equals(epsilon))
            res = true;
        return res;
    }

    // 0型文法
    public boolean isPSG(String left,String right){
        boolean res = false;
        if(!left.equals(epsilon)) res = true;
        return res;
    }

    // 1型文法
    public boolean isCSG(String left,String right){
        boolean res = false;
        // 取出epsilon后的长度
        int lenLeft = left.replaceAll(epsilon,"").length();
        int lenRight = right.replaceAll(epsilon,"").length();
        if(lenLeft <= lenRight)// 左边长度小于等于右边长度
            for(char c : left.toCharArray()){
                if(VN.contains("" +c )){// 左边至少有一个非终结符
                    res = true;
                    break;
                }
            }
        return res;
    }

    // 2型文法
    public boolean isCFG(String left,String right){
        boolean res = false;
        if(VN.contains(left)){
            res = true;

        }
        return res;
    }

    // 3型，左线性文法
    public boolean isLRG(String left,String right){
        boolean res = false;
        if(VN.contains(left))// 左边为单个非终结符
            // 第一个字符为非终结符，后面的字符为终结符
            if(right.length() >= 2 && VN.contains(right.substring(0,1))) {
                res = true;
                for (int i = 1; i < right.length(); i++) {
                    if (!VT.contains("" + right.charAt(i))) {
                        res = false;
                        break;
                    }
                }
            }
        return res;
    }

    // 3型，右线性文法
    public boolean isRRG(String left,String right){
        boolean res = false;
        if(VN.contains(left))// 左边为单个非终结符
            // 最后一个字符为非终结符，前面的字符为终结符
            if(right.length() >= 2 && VN.contains(right.substring(right.length() - 1))) {
                res = true;
                for (int i = 0; i < right.length() - 1; i++) {
                    if (!VT.contains("" + right.charAt(i))) {
                        res = false;
                        break;
                    }
                }
            }
        return res;
    }

    // 3型文法
    public boolean isRG(String left,String right){
        boolean res = false;
        if(isLRG(left,right) || isRRG(left,right))
            res = true;
        else{
            if (VN.contains(left)){
                res = true;
                for(char c : right.toCharArray()) {
                    if(VN.contains("" + c)){
                        res = false;
                        break;
                    }
                }
            }
        }
        return res;
    }
}
