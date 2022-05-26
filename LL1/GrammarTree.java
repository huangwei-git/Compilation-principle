package LL1;

import Grammar.Grammar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GrammarTree {
    GrammarTreeNode root;
    Map<String,Integer> seq = new HashMap<>();

    public GrammarTree(Grammar grammar){
        Map<String, Set<String>> productions = grammar.getProduction();
        Map<String,Set<String>> map = grammar.getProduction();
        root = getSubTree(grammar.getStart(),map,grammar.getVN());
        display(root);
        System.out.println();
        displayLeaf(root);
    }

    private GrammarTree(Grammar grammar,String Path){
        Map<String, Set<String>> productions = grammar.getProduction();
        Map<String,Set<String>> map = grammar.getProduction();
        root = getSubTree(grammar.getStart(),map,grammar.getVN());
        display(root);
        System.out.println();
        displayLeaf(root);
        generateMarkdownFile(Path);
    }

    private GrammarTreeNode getSubTree(String left, Map<String,Set<String>> map, Set<String> VN) {
        GrammarTreeNode res = new GrammarTreeNode(left);
        if(seq.containsKey(left)){
            res.id = seq.get(left) + 1;
            seq.put(left,seq.get(left) + 1);
        }else{
            res.id = 0;
            seq.put(left,0);
        }
        if(map.containsKey(left) && map.get(left).size() != 0){
            String right = map.get(left).iterator().next();
            map.get(left).remove(right);
            for(char c:right.toCharArray()){
                if(VN.contains("" + c)){
                    GrammarTreeNode child = getSubTree("" + c,map,VN);
                    res.childs.add(child);
                }else{
//                    res.childs.add(new GrammarTreeNode("" + c));
                    GrammarTreeNode child = getSubTree("" + c,map,VN);
                    res.childs.add(child);
                }
            }
        }
        return res;
    }

    private void display(GrammarTreeNode node){
        System.out.print(node.value);
        if(node.childs.size() > 0){
            System.out.print("(");
            Iterator<GrammarTreeNode> iterator = node.childs.iterator();
            while(iterator.hasNext()){
                GrammarTreeNode next = iterator.next();
                display(next);
            }
            System.out.print(")");
        }
    }

    private void displayLeaf(GrammarTreeNode node) {
        if (node.childs.size() == 0) System.out.print(node.value);
        else {
            for (GrammarTreeNode next : node.childs) {
                displayLeaf(next);
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
