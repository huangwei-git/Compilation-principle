package Thompson;
/*
() [] ^* ^+ · |
*/

import java.util.Stack;
import java.util.TreeMap;

public class Run {
    static TreeMap<String,Integer> map = new TreeMap<>(){{
       put("*",3);
       put("x",2);
       put("/",2);
       put("+",1);
       put("-",1);
       put("\0",-1);
    }};
    static int idx = -1;

    public static Node  calc(Node node1,Node node2,String op){
        return new Node(op,node1,node2);
    }

    public static Node build(String s){
        //操作数栈
        Stack<Node> sp1 = new Stack<>();
        //操作符栈
        Stack<String> sp2 = new Stack<>();
        sp2.add("\0");
        while(++idx < s.length()){
            String tmp = s.substring(idx,idx + 1);
            if(tmp.equals(")")){
                break;
            }
            if(tmp.equals("(")){
                sp1.add(build(s));
            }
            //如果是操作符
            else if(map.keySet().contains(tmp)){
                //操作符入栈
                if(map.get(tmp) > map.get(sp2.peek())){
                    sp2.add(tmp);
                }
                //计算栈内数字后入栈
                else{
                    String pop = sp2.pop();
                    //单目
                    if(pop.equals("*")){
                        sp1.add(calc(null,sp1.pop(),pop));
                    }
                    //双目
                    else{
                        sp1.add(calc(sp1.pop(),sp1.pop(),pop));
                    }
                    //计算完后,信操作符入栈
                    sp2.add(tmp);
                }
            }
            //如果是操作数
            else{
                sp1.add(new Node(tmp));
            }
        }
        while(!sp2.peek().equals("\0")){
            if(sp2.peek().equals("*"))
                sp1.add(calc(null,sp1.pop(),sp2.pop()));
            else
                sp1.add(calc(sp1.pop(),sp1.pop(),sp2.pop()));
        }
        return sp1.pop();
    }

    public static void pre(Node node,int height){
        if(node != null){
            pre(node.rchild,height + 1);
            System.out.print(node.value);
            pre(node.lchild,height + 1);
        }
    }

    public static void main(String[] args) {
        Node node = build("1+2*x(3-6x(8-2*)*)+5");
        pre(node,0);
    }
}


class Node{
    String value;
    Node lchild;
    Node rchild;

    public Node(String value){
        this.value = value;
        lchild = rchild = null;
    }

    public Node(String value,Node lchild,Node rchild){
        this.value = value;
        this.lchild = lchild;
        this.rchild = rchild;
    }
}