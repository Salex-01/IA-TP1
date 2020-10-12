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

    // Retourne la liste des actions à faire pour aller de la racine de l'arbre à ce noeud
    public LinkedList<Character> traceBack() {
        if (parent == null) {
            return new LinkedList<>();
        }
        LinkedList<Character> l = parent.traceBack();
        l.add(transition);
        return l;
    }

    // Crée les noeuds enfants selon les actions possibles depuis le noeud actuel
    public ArrayList<Node> generateChildren() {
        ArrayList<Node> c = new ArrayList<>();
        // Si la case contient un bijou
        if ((treeState.map[treeState.x][treeState.y] & Constants.JEWEL) != 0) {
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.JEWEL);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.PICK, this));
        } else if ((treeState.map[treeState.x][treeState.y] & Constants.DUST) != 0) {
            // Si la case contient de la poussière
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.DUST);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.SUCK, this));
        }
        // Si l'Aspirobot peut aller à gauche
        if (treeState.x > 0) {
            c.add(new Node(new TreeState(treeState.x - 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.LEFT, this));
        }
        // Si l'Aspirobot peut aller à droite
        if (treeState.x < treeState.map.length - 1) {
            c.add(new Node(new TreeState(treeState.x + 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.RIGHT, this));
        }
        // Si l'Aspirobot peut aller vers le haut
        if (treeState.y > 0) {
            c.add(new Node(new TreeState(treeState.x, treeState.y - 1, treeState.map, treeState.electricityScore + 1), Constants.UP, this));
        }
        // Si l'Aspirobot peut aller vers le bas
        if (treeState.y < treeState.map[0].length - 1) {
            c.add(new Node(new TreeState(treeState.x, treeState.y + 1, treeState.map, treeState.electricityScore + 1), Constants.DOWN, this));
        }
        return c;
    }
}