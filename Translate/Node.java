package Translate;

//树结点类
public class Node{
    private String value;
    private Node leftChild;
    private Node RightChild;

    // 构造方法，初始化value
    public Node(String value){
        this.value = value;
        leftChild = RightChild = null;
    }

    // 构造方法，初始化value,lchild,rchild
    public Node(String value,Node lchild,Node rchild){
        this.value = value;
        this.leftChild = lchild;
        this.RightChild = rchild;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return RightChild;
    }

    public void setRightChild(Node rightChild) {
        this.RightChild = rightChild;
    }
}