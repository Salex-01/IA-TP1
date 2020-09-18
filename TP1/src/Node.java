import java.util.ArrayList;
import java.util.List;

public class Node {
    TreeState treeState;
    Character t;
    Node parent;
    ArrayList<Node> children;

    public Node(TreeState s, char t1, Node p) {
        treeState = s;
        t = t1;
        parent = p;
        children = new ArrayList<Node>();
    }

    void addChildren(Node child) {
        children.add(child);
    }

    Boolean equal(Node n) {
        if (treeState == n.treeState && t == n.t && parent.equals(parent)) {
            return true;
        }
        return false;
    }
}

