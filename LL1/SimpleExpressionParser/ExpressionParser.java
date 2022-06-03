package LL1.SimpleExpressionParser;

import java.io.*;

// E' -> Q
// T' -> W

public class ExpressionParser {
    private char lookAHead;// 当前指向的字符
    private CharArrayReader expressionReader;// 读取表达式的字符流
    private Node root;// 表达式解析树根结点

    // 读取路径中的文件内容作为参数，实例化CharArrayReader字符输入流对象
    public void loadExpression(String filePath){
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

    public void work(String filePath){
        loadExpression(filePath);
        // 调用pares解析表达式
        parse();
        // 先序遍历表达式解析树
        root.preOrder();
        System.out.println();
        int pos = filePath.lastIndexOf('/');
        root.generateMarkdownFile(filePath.substring(0,pos + 1));
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

    public CharArrayReader getExpressionReader() {
        return expressionReader;
    }
}

// 解析错误异常类类
class ExpressionException extends Throwable {

    public ExpressionException(String s) {
        super();
        System.out.println("Production error : " + s);
    }
}

