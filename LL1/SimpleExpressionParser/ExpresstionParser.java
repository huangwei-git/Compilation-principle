package LL1.SimpleExpressionParser;

import Grammar.Grammar;
import LL1.FIRSTandFOLLOW;
import LL1.SyntaxTree.GrammarTreeNode;

import java.io.*;
import java.security.DrbgParameters;
import java.util.Scanner;

public class ExpresstionParser {
    private char lookAHead;
    private CharArrayReader expressionReader;
    private GrammarTreeNode root;

    public ExpresstionParser(String s,int i) {
        String expression = s.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
        expressionReader = new CharArrayReader(expression.toCharArray());
    }

    public ExpresstionParser(String fileName) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(fileName));
            String expression = new String(fis.readAllBytes());
            expression = expression.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
            expressionReader = new CharArrayReader(expression.toCharArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件输入流fis
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    public void parse(){
        getNextToken();
        root = E();
    }

    public void preorder(GrammarTreeNode node){
        if(node != null){
            System.out.print(node.value);
            if(node.childs.size() > 0) System.out.print("(");
            for(GrammarTreeNode child : node.childs){
                preorder(child);
            }
            if(node.childs.size() > 0) System.out.print(")");
        }
    }

    private GrammarTreeNode E(){
        GrammarTreeNode currentNode = new GrammarTreeNode("E");
        //E -> TQ
        // E: [(, i]
        if(lookAHead == '(' || lookAHead == 'i'){
            currentNode.childs.add(T());
            currentNode.childs.add(Q());
        }
        else{
            // exception
            try {
                throw new ExpressionException("E->TQ");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private GrammarTreeNode Q(){
        GrammarTreeNode currentNode = new GrammarTreeNode("Q");
        // Q -> ATQ
        if(lookAHead == '(' || lookAHead == 'i' || lookAHead == '+' || lookAHead == '-'){
            currentNode.childs.add(A());
            currentNode.childs.add(T());
            currentNode.childs.add(Q());
        }
        return currentNode;
    }

    private GrammarTreeNode T(){
        GrammarTreeNode currentNode = new GrammarTreeNode("T");
        // T->FW
        // T: [(, i]
        if(lookAHead == '(' || lookAHead == 'i'){
            currentNode.childs.add(F());
            currentNode.childs.add(W());
        }
        else{
            // exception
            try {
                throw new ExpressionException("T->FW");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private GrammarTreeNode W(){
        GrammarTreeNode currentNode = new GrammarTreeNode("W");
        // W->MFW
        if(lookAHead == '(' || lookAHead == 'i' || lookAHead == '*' || lookAHead == '/'){
            currentNode.childs.add(M());
            currentNode.childs.add(F());
            currentNode.childs.add(W());
        }
        return currentNode;
    }

    private GrammarTreeNode F(){
        GrammarTreeNode currentNode = new GrammarTreeNode("F");
        //F->(E)
        if(lookAHead == '('){
            currentNode.childs.add(new GrammarTreeNode("("));
            getNextToken();
            currentNode.childs.add(new GrammarTreeNode("E"));
            getNextToken();
            if(lookAHead == ')'){
                currentNode.childs.add(new GrammarTreeNode(")"));
                getNextToken();//???
            }else try { // exception
                throw new ExpressionException("F->(E)");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        // F -> i
        else if(lookAHead == 'i'){
            currentNode.childs.add(new GrammarTreeNode("i"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("F->i");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private GrammarTreeNode A(){
        GrammarTreeNode currentNode = new GrammarTreeNode("A");
        // A -> +
        if(lookAHead == '+'){
            currentNode.childs.add(new GrammarTreeNode("+"));
            getNextToken();
        }
        // A-> -
        else if(lookAHead == '-'){
            currentNode.childs.add(new GrammarTreeNode("-"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("A->+|-");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    private GrammarTreeNode M(){
        GrammarTreeNode currentNode = new GrammarTreeNode("F");
        // M -> *
        if(lookAHead == '*'){
            currentNode.childs.add(new GrammarTreeNode("*"));
            getNextToken();
        }
        // M -> /
        else if(lookAHead == '/'){
            currentNode.childs.add(new GrammarTreeNode("/"));
            getNextToken();
        }
        else{
            // exception
            try {
                throw new ExpressionException("M->*|/");
            } catch (ExpressionException e) {
                e.printStackTrace();
            }
        }
        return currentNode;
    }

    public char getLookAHead() {
        return lookAHead;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        ExpresstionParser expresstionParser = new ExpresstionParser("i+i*i",0);
        expresstionParser.parse();
        expresstionParser.preorder(expresstionParser.root);
    }
}

class ExpressionException extends Throwable {

    public ExpressionException(String s){
        System.out.println("Production error : " + s);
    }
}