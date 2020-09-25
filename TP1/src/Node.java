import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
    TreeState treeState;
    Character transition;
    Node parent;

    public Node(TreeState s, char t1, Node p) {
        treeState = s;
        transition = t1;
        parent = p;
    }

    public LinkedList<Character> traceBack() {
        if (parent == null) {
            return new LinkedList<>();
        }
        LinkedList<Character> l = parent.traceBack();
        l.add(transition);
        return l;
    }

    public ArrayList<Node> generateChildren() {
        ArrayList<Node> c = new ArrayList<>();
        if (treeState.x > 0) {
            c.add(new Node(new TreeState(treeState.x - 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.LEFT, this));
        }
        if (treeState.x < treeState.map.length - 1) {
            c.add(new Node(new TreeState(treeState.x + 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.RIGHT, this));
        }
        if (treeState.y > 0) {
            c.add(new Node(new TreeState(treeState.x, treeState.y - 1, treeState.map, treeState.electricityScore + 1), Constants.UP, this));
        }
        if (treeState.y < treeState.map[0].length - 1) {
            c.add(new Node(new TreeState(treeState.x, treeState.y + 1, treeState.map, treeState.electricityScore + 1), Constants.DOWN, this));
        }
        if ((treeState.map[treeState.x][treeState.y] & Constants.JEWEL) != 0) {
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.JEWEL);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.PICK, this));
        } else if ((treeState.map[treeState.x][treeState.y] & Constants.DUST) != 0) {
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.DUST);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.SUCK, this));
        }
        return c;
    }
}