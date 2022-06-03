package LL1.SimpleExpressionParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 表达式解析树结点类
public class Node{
    public String value;
    public List<Node> childs;

    public Node(String value) {
        this.value = value;
        childs = new LinkedList<>();
    }

    // 先序遍历
    public void preOrder(){
        System.out.print(value);
        if(childs.size() > 0){
            System.out.print("(");
            for(Node child : childs){
                child.preOrder();
            }
            System.out.print(")");
        }
    }

    public void generateMarkdownFile(String filePath){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filePath + "ParseTree.md")));
            Map<String,Integer> map = new HashMap<>(){{
                put("E",0);put("Q",0);put("T",0);put("W",0);put("F",0);put("A",0);put("M",0);
            }};
            bw.write("```mermaid\n" +
                    "graph TB\n");
            generateMarkdownContent(map,bw);
            bw.write("```");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw != null){
                try {
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void generateMarkdownContent(Map<String,Integer> map,BufferedWriter bw) throws IOException {
        String leftValue = value;
        /*替换掉会出现的特殊字符*/
        if(value.equals("-")) leftValue = "minus";
        else if(value.equals("(")) leftValue = "LB";
        else if(value.equals(")")) leftValue = "RB";
        String left = leftValue+getSeq(map,leftValue) + "((\"" + value + "\"))";
        map.put(leftValue,map.get(leftValue) + 1);
        if(childs.size() > 0){
            for(Node child : childs){
                String childValue = child.value;
                if(childValue.equals("-")) childValue = "minus";
                else if(childValue.equals("(")) childValue = "LB";
                else if(childValue.equals(")")) childValue = "RB";
                String right = childValue+ getSeq(map,childValue) + "((\"" + child.value + "\"))";
                bw.write(left + "-.-" + right + "\n");
                child.generateMarkdownContent(map,bw);
                map.put(childValue,map.get(childValue) + 1);
            }
        }else{
            if(leftValue.charAt(0) >= 'A' && leftValue.charAt(0) <= 'Z'){
                String right = "ε" + getSeq(map,"ε") + "((" + "ε" + "))";
                bw.write(left + "-.-" + right+"\n");
                bw.write("style " + "ε" + map.get("ε") +" fill:#9AFF9A,stroke-dasharray: 5 5\n");
                map.put("ε",map.get("ε") + 1);
            }else{
                bw.write("style " + leftValue + (map.get(leftValue) - 1) +" fill:#FFFF00,stroke-dasharray: 5 5\n");
            }
        }
    }

    private int getSeq(Map<String,Integer> map,String value){
        int res = 0;
        if(map.containsKey(value)){
            res = map.get(value);
        }else map.put(value,0);
        return res;
    }
}