import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
    TreeState treeState;
    Character transition;
    Node parent;

    /*
    cree un noeud avec un etat de l'arbre , une transition c'est à dire l'action realise par le robot pour passer du noeud parent au noeud cree et un noeud parent
     */
    public Node(TreeState s, char t1, Node p) {
        treeState = s;
        transition = t1;
        parent = p;
    }

    /*
    parcours l'arbre de la feuille ayant un "treestate" qui répond aux désires jusqu'à la racine pour renvoyer les intentions
     */
    public LinkedList<Character> traceBack() {
        if (parent == null) {
            return new LinkedList<>();
        }
        LinkedList<Character> l = parent.traceBack();
        l.add(transition);
        return l;
    }
    /*
    Créer les Noeuds selon les actions possible depuis le noeud actuel
     */
    public ArrayList<Node> generateChildren() {
        ArrayList<Node> c = new ArrayList<>();
        /*
        Creer un noeud si l'aspirobot peut ramasser un bijou
         */
        if ((treeState.map[treeState.x][treeState.y] & Constants.JEWEL) != 0) {
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.JEWEL);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.PICK, this));
        } else if ((treeState.map[treeState.x][treeState.y] & Constants.DUST) != 0) {
            /*
            Creer un noeud si l'aspirobot peut aspirer la poussière
            */
            int[][] newMap = Constants.deepClone(treeState.map);
            newMap[treeState.x][treeState.y] &= (~Constants.DUST);
            c.add(new Node(new TreeState(treeState.x, treeState.y, newMap, treeState.electricityScore + 1), Constants.SUCK, this));
        }
        /*
         Creer un noeud si l'aspirobot peut se deplacer à gauche
        */
        if (treeState.x > 0) {
            c.add(new Node(new TreeState(treeState.x - 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.LEFT, this));
        }
        /*
         Creer un noeud si l'aspirobot peut se deplacer à droite
        */
        if (treeState.x < treeState.map.length - 1) {
            c.add(new Node(new TreeState(treeState.x + 1, treeState.y, treeState.map, treeState.electricityScore + 1), Constants.RIGHT, this));
        }
        /*
         Creer un noeud si l'aspirobot peut se deplacer en haut
        */
        if (treeState.y > 0) {
            c.add(new Node(new TreeState(treeState.x, treeState.y - 1, treeState.map, treeState.electricityScore + 1), Constants.UP, this));
        }
        /*
         Creer un noeud si l'aspirobot peut se deplacer en bas
        */
        if (treeState.y < treeState.map[0].length - 1) {
            c.add(new Node(new TreeState(treeState.x, treeState.y + 1, treeState.map, treeState.electricityScore + 1), Constants.DOWN, this));
        }
        return c;
    }
}