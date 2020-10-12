import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Aspirobot extends Thread {

    Sensors sensors;
    Effectors effectors;
    Environment e;
    int posX = 0;
    int posY = 0;
    int[][] beliefs;

    /*
    teste si les desires de l'aspirobot sont realises à l'etat de l'arbre donne en argument
     */
    boolean desires(TreeState state) {
        for (int i = 0; i < state.map.length; i++) {
            for (int j = 0; j < state.map[0].length; j++) {
                if ((state.map[i][j] & (Constants.DUST | Constants.JEWEL)) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    LinkedList<Character> intentions;
    String decideMode;
    int limit;

    boolean stopped = false;
    /*
    constructeur de l'aspirobot
     */
    public Aspirobot(Environment e, String mode, int lim) {
        this.e = e;
        sensors = new Sensors(e, this);
        effectors = new Effectors(e, this);
        decideMode = mode;
        limit = lim;
        this.start();
    }

    /*
    boucle d'execution de l'aspirobot
     */
    @Override
    public void run() {
        int intentionIndex;
        while (!stopped) {
            intentionIndex = 0;
            Main.updateGraphics(false, true);
            // l'aspirobot utilise ses "capteurs" pour mettre à jour "beliefs"
            sensors.observe();
            // decide des intentions suivant le mode de recherche
            switch (decideMode) {
                case "n_w":
                    intentions = decideN_W();
                    break;
                case "n_d":
                    intentions = decideN_D();
                    break;
                case "i_bf":
                    intentions = decideI_BF();
                    break;
                default:
                    intentions = new LinkedList<>();
                    System.out.println("Unknown mode");
                    break;
            }
            for (char c : intentions) {
                /*
                parcours les intentions pour les faire executer par les effecteurs de l'aspirobot
                 */
                switch (c) {
                    case Constants.SUCK:
                        effectors.suck();
                        break;
                    case Constants.PICK:
                        effectors.pick();
                        break;
                    default:
                        effectors.move(c);
                        break;
                }
                Main.updateGraphics(true, false);
                intentionIndex++;
                /*
                si le nombre d'intentions depasse la limite les intententions apres cette limite sont ignores
                 */
                if (intentionIndex >= limit) {
                    break;
                }
            }
        }
    }

    private boolean initDecide(LinkedList<TreeState> knownStates, LinkedList<Node> notVisited, boolean cs) {
        Node root = new Node(new TreeState(posX, posY, beliefs, 0), Constants.INIT, null);
        if (desires(root.treeState)) {
            return true;
        }
        knownStates.add(root.treeState);
        notVisited.add(root);
        if (cs) {
            root.treeState.computeScore();
        }
        return false;
    }

    private LinkedList<Character> decideN_W() { //Exploration non informee en largeur
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if(initDecide(knownStates,notVisited,false)){
            return new LinkedList<>();
        }
        boolean b;
        List<Node> list;
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            for (Node n : list) {
                if (desires(n.treeState)) {
                    return n.traceBack();
                }
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    knownStates.add(n.treeState);
                    notVisited.add(notVisited.size(), n);
                }
            }
        }
        return new LinkedList<>();
    }

    private LinkedList<Character> decideN_D() { //Exploration non informee en profondeur
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if(initDecide(knownStates,notVisited,false)){
            return new LinkedList<>();
        }
        int i;
        boolean b;
        List<Node> list;
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            i = 0;
            for (Node n : list) {
                if (desires(n.treeState)) {
                    return n.traceBack();
                }
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    knownStates.add(n.treeState);
                    notVisited.add(i, n);
                    i++;
                }
            }
        }
        return new LinkedList<>();
    }

    private LinkedList<Character> decideI_BF() {    // Exploration informee best first
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if (initDecide(knownStates, notVisited, true)) {
            return new LinkedList<>();
        }
        boolean b;
        List<Node> list;
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            for (Node n : list) {
                if (desires(n.treeState)) {
                    return n.traceBack();
                }
                n.treeState.computeScore(); // Calcul de la desirabilite de l'etat
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    knownStates.add(n.treeState);
                    notVisited.add(n);
                }
            }
            notVisited.sort(Comparator.comparingDouble(o -> o.treeState.score));
        }
        return new LinkedList<>();
    }

    public void sStop() {
        stopped = true;
    }
}