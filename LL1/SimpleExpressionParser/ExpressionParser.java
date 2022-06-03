package LL1.SimpleExpressionParser;

import ThompsonBuildNFA.Pair;

import java.io.*;
import java.util.*;

// E' -> Q
// T' -> W

public class ExpressionParser {
    private char lookAHead;// 当前指向的字符
    private CharArrayReader expressionReader;// 读取表达式的字符流
    private Node root;// 表达式解析树根结点

    public ExpressionParser(String filePath) {
        loadExpression(filePath);
        // 调用pares解析表达式
        parse();
        // 先序遍历表达式解析树
        root.preOrder();
        System.out.println();
        int pos = filePath.lastIndexOf('/');
        root.generateMarkdownFile(filePath.substring(0,pos + 1));
    }

    // 读取路径中的文件内容作为参数，实例化CharArrayReader字符输入流对象
    private void loadExpression(String filePath){
        try {
            // 读取文件，获得表达式
            String expression = new String(new FileInputStream(new File(filePath)).readAllBytes());
            // 消除whilespaces
            expression = expression.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
            // 将表达式的char[]数组作为参数构造CharArrayReader
            expressionReader = new CharArrayReader(expression.toCharArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获得表达式的下一次单词
    private char getNextToken(){
        char next = '#';
        try {
            int read;
            if((read = (char) expressionReader.read()) != 65535) next = (char)read;
        } catch (IOException e) {
            e.printStackTrace();
        }
        lookAHead = next;
        return next;
    }

    // 从文法的起始符开始解析表达式
    private void parse(){
        // 获取第一个字符
        getNextToken();
        // 从起始符开始解析，将返回的解析树赋值给root
        root = E();
    }

    private Node E(){
        Node currentNode = new Node("E");
        //E -> TQ
        // E: [(, i]
        if(lookAHead == '(' || lookAHead == 'i'){
            currentNode.childs.add(T());
            currentNode.childs.add(Q());
        }
        else{
            // exception
            try {
                throw new ExpressionException("[" + lookAHead + "]  E->TQ");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private Node Q(){
        Node currentNode = new Node("Q");
        // Q -> ATQ
        if(lookAHead == '(' || lookAHead == 'i' || lookAHead == '+' || lookAHead == '-'){
            currentNode.childs.add(A());
            currentNode.childs.add(T());
            currentNode.childs.add(Q());
        }
        return currentNode;
    }

    private Node T(){
        Node currentNode = new Node("T");
        // T->FW
        // T: [(, i]
        if(lookAHead == '(' || lookAHead == 'i'){
            currentNode.childs.add(F());
            currentNode.childs.add(W());
        }
        else{
            // exception
            try {
                throw new ExpressionException("[" + lookAHead + "]  T->FW");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private Node W(){
        Node currentNode = new Node("W");
        // W->MFW
        if(lookAHead == '(' || lookAHead == 'i' || lookAHead == '*' || lookAHead == '/'){
            currentNode.childs.add(M());
            currentNode.childs.add(F());
            currentNode.childs.add(W());
        }
        return currentNode;
    }

    private Node F(){
        Node currentNode = new Node("F");
        //F->(E)
        if(lookAHead == '('){
            currentNode.childs.add(new Node("("));
            getNextToken();
            currentNode.childs.add(E());
            if(lookAHead == ')'){
                currentNode.childs.add(new Node(")"));
                getNextToken();//???
            }else try { // exception
                throw new ExpressionException("[" + lookAHead + "]  F->(E)");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        // F -> i
        else if(lookAHead == 'i'){
            currentNode.childs.add(new Node("i"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("[" + lookAHead + "]  F->i");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private Node A(){
        Node currentNode = new Node("A");
        // A -> +
        if(lookAHead == '+'){
            currentNode.childs.add(new Node("+"));
            getNextToken();
        }
        // A-> -
        else if(lookAHead == '-'){
            currentNode.childs.add(new Node("-"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("[" + lookAHead + "]  A->+|-");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private Node M(){
        Node currentNode = new Node("F");
        // M -> *
        if(lookAHead == '*'){
            currentNode.childs.add(new Node("*"));
            getNextToken();
        }
        // M -> /
        else if(lookAHead == '/'){
            currentNode.childs.add(new Node("/"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("[" + lookAHead + "]  M->*|/");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }
}

// 解析错误异常类类
class ExpressionException extends Throwable {

    public ExpressionException(String s) {
        super();
        System.out.println("Production error : " + s);
    }
}

// 表达式解析树结点类
class Node{
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