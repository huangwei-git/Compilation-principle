package ThompsonBuildNFA;

//树结点类
public class Node{
    private String value;
    private Node leftChild;
    private Node RightChild;
    //该结点是否有括号
    private boolean brackets;

    public Node(String value){
        brackets = false;
        this.value = value;
        leftChild = RightChild = null;
    }

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

    public boolean isBrackets() {
        return brackets;
    }

    public void setBrackets(boolean brackets) {
        this.brackets = brackets;
    }
}