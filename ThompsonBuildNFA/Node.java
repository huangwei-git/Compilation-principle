package ThompsonBuildNFA;

//树结点类
public class Node{
    String value;
    Node lchild;
    Node rchild;
    //该结点是否有括号
    int brackets = 0;

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