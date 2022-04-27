import Thompson.BuildTree;
import Thompson.Node;

public class Run{
    public static void main(String[] args) {
        BuildTree buildTree = new BuildTree();
        Node node = buildTree.getTree("((0*|1)(1*0))*");
        buildTree.inOrderTraversal(node,0);
    }
}