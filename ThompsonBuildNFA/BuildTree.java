package ThompsonBuildNFA;
/*
运算符优先级
() [] ^* ^+ · |
*/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

//根据正规式创建运算树
public class BuildTree {
    // 操作符优先级映射
    private HashMap<String,Integer> operatorPriorityMap = new HashMap<>(){{
        put("*",4);
        put("+",3);
        put("·",2);
        put("|",1);
        put("\0",-1);
    }};
    // 左括号
    private HashSet<String> leftBracket = new HashSet<>(){{
       add("(");
    }};
    // 右括号
    private HashSet<String> rightBracket = new HashSet<>(){{
       add(")");
    }};
    // 单目运算符
    private HashSet<String> unaryOperators = new HashSet<>(){{
        add("*");
        add("+");
    }};
    private int idx = -1;
    private boolean lastIsOperator = true;//判断上一个符号是否为运算符，用于识别是否有"·"运算符被省略

    // 计算Node1 op Node2
    private Node calc(Node node1,Node node2,String op){
        return new Node(op,node1,node2);
    }

    //根据正规式s构建运算树
    private Node buildTree(String normalExpression){
        //操作数栈
        Stack<Node> operandStack = new Stack<>();
        //操作符栈
        Stack<String> operatorStack = new Stack<>();
        operatorStack.add("\0");// 栈底压入哨兵字符串
        while(++idx < normalExpression.length()){
            String nextSymbol = normalExpression.substring(idx,idx + 1);
            // 右括号
            if(rightBracket.contains(nextSymbol)) break;
            // 如果连续两个操作数，则中间补充一个·运算符
            if((!lastIsOperator || operatorStack.peek().equals("*")) && !operatorPriorityMap.keySet().contains(nextSymbol)){
                nextSymbol = "·";
                --idx;
            }
            // 左括号
            if(leftBracket.contains(nextSymbol)){
                Node get = buildTree(normalExpression);
                get.brackets = 1;
                operandStack.add(get);
                lastIsOperator = false;
            }
            //如果是操作符
            else if(operatorPriorityMap.keySet().contains(nextSymbol)){
                lastIsOperator = true;
                //操作符入栈
                if(operatorPriorityMap.get(nextSymbol) > operatorPriorityMap.get(operatorStack.peek())){
                    if(unaryOperators.contains(nextSymbol)) lastIsOperator = false;
                    operatorStack.add(nextSymbol);
                }
                //计算栈内数字后入栈
                else{
                    while(operatorPriorityMap.get(nextSymbol) < operatorPriorityMap.get(operatorStack.peek())){
                        String popOperator = operatorStack.pop();
                        // 栈顶元素是单目运算符
                        if(unaryOperators.contains(popOperator)){
                            operandStack.add(calc(null,operandStack.pop(),popOperator));
                        }
                        // 栈顶操作符是双目运算符
                        else{
                            operandStack.add(calc(operandStack.pop(),operandStack.pop(),popOperator));
                        }
                    }
                    //计算完后,信操作符入栈
                    operatorStack.add(nextSymbol);
                }
            }
            //如果是操作数
            else{
                lastIsOperator = false;
                operandStack.add(new Node(nextSymbol));
            }
        }
        // 计算栈内所有元素
        String popOperator = null;// 栈顶元素
        while(!operatorStack.peek().equals("\0")){
            popOperator = operatorStack.pop();
            //栈顶是单目运算符
            if(unaryOperators.contains(popOperator))
                operandStack.add(calc(null,operandStack.pop(),popOperator));
            //栈顶是双目运算符
            else
                operandStack.add(calc(operandStack.pop(),operandStack.pop(),popOperator));
        }
        return operandStack.pop();
    }

    // 根据正规式获取运算符树
    public Node getTree(String normalExpression){
        idx = -1;// 初始化
        return buildTree(normalExpression);
    }

    // 中序遍历
    public void inOrderTraversal(Node node){
        if(node != null){
            if(node.brackets == 1) System.out.print("(");
            inOrderTraversal(node.rchild);
            if(!node.value.equals("·")) System.out.print(node.value);
            inOrderTraversal(node.lchild);
            if (node.brackets == 1) System.out.print(")");
        }
    }
}
