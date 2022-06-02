package LL1.SyntaxTree;

import Grammar.Grammar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GrammarTree {
    GrammarTreeNode root;
    // 记录结点编号，用于生成markdown文档
    Map<String,Integer> seq = new HashMap<>();

    public GrammarTree(Grammar grammar){
        Map<String, Set<String>> productions = grammar.getProduction();
        Map<String,Set<String>> map = grammar.getProduction();
        root = GenerateTree(grammar.getStart(),map);
        getSyntaxTreeString(root);
        System.out.println();
        getLeafString(root);
    }

    public GrammarTree(Grammar grammar,String Path){
        Map<String, Set<String>> productions = grammar.getProduction();
        root = GenerateTree(grammar.getStart(),grammar.getProduction());
        getSyntaxTreeString(root);
        System.out.println();
        getLeafString(root);
        generateMarkdownFile(Path);
    }

    private GrammarTreeNode GenerateTree(String left, Map<String,Set<String>> currentProductions) {
        // 创建一个值为left的结点res，其作为函数的返回结点
        GrammarTreeNode ret = new GrammarTreeNode(left);
        // 对每个结点编号，用于避免生成markdown文档时，值相同的结点混乱
        if(seq.containsKey(left)){
            ret.id = seq.get(left) + 1;
            seq.put(left,seq.get(left) + 1);
        }else{
            ret.id = 0;
            seq.put(left,0);
        }
        // 判断map是否包含map(若left∈VN，则map不包含left)，并且判断该产生式的右部是否用完
        if(currentProductions.containsKey(left) && currentProductions.get(left).size() != 0){
            // 从map中取出一个left对应的产生式右部 right
            String right = currentProductions.get(left).iterator().next();
            // 将取出的right从map集合中删除
            currentProductions.get(left).remove(right);
            // 遍历产生式右部的所有字符，作为当前结点的子结点，并递归建立子结点对应的子树
            for(char c:right.toCharArray()){
                    GrammarTreeNode child = GenerateTree("" + c,currentProductions);
                    ret.childs.add(child);
            }
        }
        return ret;
    }

    private void getSyntaxTreeString(GrammarTreeNode node){
        System.out.print(node.value);
        if(node.childs.size() > 0){
            System.out.print("(");
            Iterator<GrammarTreeNode> iterator = node.childs.iterator();
            while(iterator.hasNext()){
                GrammarTreeNode next = iterator.next();
                getSyntaxTreeString(next);
            }
            System.out.print(")");
        }
    }

    private void getLeafString(GrammarTreeNode node) {
        if (node.childs.size() == 0) System.out.print(node.value);
        else {
            for (GrammarTreeNode next : node.childs) {
                getLeafString(next);
            }
        }
    }

    private void generateMarkdownFile(String path){
        FileWriter fw = null;
        try {
             fw = new FileWriter(new File(path));
             StringBuilder output = new StringBuilder();
             output.append("```mermaid\n" +
                     "graph TB\n");
             output.append(getMarkdown(root));
             output.append("```");
             fw.write(output.toString());
             fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getMarkdown(GrammarTreeNode node){
        StringBuilder res = new StringBuilder();
        String nodeValue = node.value + node.id;
        if(node.childs.size() > 0){
            Iterator<GrammarTreeNode> iterator = node.childs.iterator();
            while(iterator.hasNext()){
                GrammarTreeNode next = iterator.next();
                res.append(nodeValue + "((" + node.value + "))-.->" + next.value + next.id + "(("+next.value+"))\n");
                if(next.childs.size() == 0) res.append("style "+ next.value + next.id +" fill:#9AFF9A,stroke-dasharray: 5 5\n");
                res.append(getMarkdown(next));
            }
        }
        return res.toString();
    }
}
