package Translate;
/*
运算符优先级
() [] ^* ^+ · |
*/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

//根据正规式创建运算树
public class InversePolish {
/*======Arrtibute======*/

    // 运算符优先级映射
    private static final HashMap<String,Integer> operatorPriorityMap = new HashMap(){{
        put("/",2);
        put("*",2);
        put("+",1);
        put("-",1);
        put("#",0);
    }};
    // 左括号
    private static final HashSet<String> leftBracket = new HashSet(){{
       add("(");
    }};
    // 右括号
    private static final HashSet<String> rightBracket = new HashSet(){{
       add(")");
    }};
    // 单目运算符
    private static final HashSet<String> unaryOperators = new HashSet(){{

    }};
    private static int idx;
    //判断上一个符号是否为运算符，用于识别是否有"·"运算符被省略
    private static String normalExpression;

/*======Method======*/

    // 根据正规式获取运算符树
    public static Node getTree(String Expression){
        // 初始化
        idx = -1;
        normalExpression = Expression;
        return buildTree();
    }


    public void postOrder(Node node){
        if(node != null){
            postOrder(node.getRightChild());
            postOrder(node.getLeftChild());
            System.out.print(node.getValue());
        }
    }

    // 将左右子树结合起来，根结点的值为op
    private static Node combine(Node rightNode, Node leftNode, String op){
        return new Node(op,leftNode,rightNode);
    }

    // 获取下一个字符
    private static String getNextToken(){
        String res = null;
        if(++idx < normalExpression.length()){
            res = normalExpression.substring(idx,idx + 1);
        }
        return res;
    }

    //根据表达式构建运算树
    private static Node buildTree(){
        // 操作数栈
        Stack<Node> numStack = new Stack<>();
        // 运算符栈
        Stack<String> opStack = new Stack<>();
        opStack.add("#");// 栈底压入哨兵字符串
        String currentSymbol;
        while((currentSymbol = getNextToken()) != null){
            // 右括号：递归出口
            if(rightBracket.contains(currentSymbol)) break;
            // 左括号：递归入口
            if(leftBracket.contains(currentSymbol)){
                // 存储递归得到的子树压入操作数栈
                numStack.add(buildTree());
            }
            // 如果是运算符
            else if(operatorPriorityMap.keySet().contains(currentSymbol)){
                // 运算符入栈
                if(operatorPriorityMap.get(currentSymbol) > operatorPriorityMap.get(opStack.peek())){
                    opStack.add(currentSymbol);
                }
                // 计算栈内数字后入栈
                else{
                    while(operatorPriorityMap.get(currentSymbol) < operatorPriorityMap.get(opStack.peek())){
                        String popOperator = opStack.pop();
                        // 栈顶元素是单目运算符
                        if(unaryOperators.contains(popOperator)){
                            numStack.add(combine(null,numStack.pop(),popOperator));
                        }
                        // 栈顶运算符是双目运算符
                        else{
                            numStack.add(combine(numStack.pop(),numStack.pop(),popOperator));
                        }
                    }
                    // 计算完后,返回的运算符符入栈
                    opStack.add(currentSymbol);
                }
            }
            // 如果是操作数
            else{
                numStack.add(new Node(currentSymbol));
            }
        }
        // 计算栈内所有元素
        String popOperator = null;// 栈顶元素
        while(!opStack.peek().equals("#")){
            popOperator = opStack.pop();
            // 栈顶是单目运算符
            if(unaryOperators.contains(popOperator))
                numStack.add(combine(null,numStack.pop(),popOperator));
            // 栈顶是双目运算符
            else
                numStack.add(combine(numStack.pop(),numStack.pop(),popOperator));
        }
        // 返回操作数栈的栈顶元素，即表达式对应运算数的根结点
        return numStack.pop();
    }
}
