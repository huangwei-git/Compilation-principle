package LL1.SyntaxTree;

import Grammar.Grammar;
import LL1.FIRSTandFOLLOW;

import java.util.*;

public class AnalysisTable {

    Map<String, Map<String,List<String>>> AT;

    public AnalysisTable(Grammar grammar){
        int lenVN = grammar.getVN().size();
        int lenVT = grammar.getVT().size();
        AT = new HashMap<>();
        //初始化预测分析表的行和列
        Iterator<String> iterator = grammar.getVN().iterator();
        int idx = 0;
        while(iterator.hasNext()){
            String left = iterator.next();
            AT.put(left,new HashMap<>());
            grammar.getVT();
            for(String vt : grammar.getVT()){
                AT.get(left).put(vt,new ArrayList<>());
            }
        }

        //构建FIRST集和FOLLOW集
        FIRSTandFOLLOW firsTandFOLLOW = new FIRSTandFOLLOW(grammar);
        iterator = grammar.getVN().iterator();
        // 遍历非终结符


    }
}
