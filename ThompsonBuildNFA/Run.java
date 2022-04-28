import ThompsonBuildNFA.BuildTree;
import ThompsonBuildNFA.Node;

import java.util.HashMap;

public class Run{
    public static void main(String[] args) {
        BuildTree buildTree = new BuildTree();
        Node node = buildTree.getTree("((0*|1)(1*0))*");
        buildTree.inOrderTraversal(node);
    }
}

class nd{
    String state;
    HashMap<String,nd> map;
}