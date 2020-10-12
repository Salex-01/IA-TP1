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

    // Teste si les désirs de l'Aspirobot sont réalisés par l'état donné en argument
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

    public Aspirobot(Environment e, String mode, int lim) {
        this.e = e;
        sensors = new Sensors(e, this);
        effectors = new Effectors(e, this);
        decideMode = mode;
        limit = lim;
        this.start();
    }

    // Boucle d'exécution de l'Aspirobot
    @Override
    public void run() {
        int intentionIndex;
        while (!stopped) {
            intentionIndex = 0;
            Main.updateGraphics(false, true);
            // L'Aspirobot utilise ses capteurs pour mettre à jour beliefs
            sensors.observe();
            // Détermination des intentions suivant le mode de recherche
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
            // Parcourt les intentions pour les faire exécuter par les effecteurs de l'Aspirobot
            for (char c : intentions) {
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
                // Si le nombre d'actions exécutées atteint la limite, les intentions suivantes sont ignorées
                if (intentionIndex >= limit) {
                    break;
                }
            }
        }
    }

    // Partie commune à l'initialisation de toutes les fonctions de décision
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

    // Exploration non informee en largeur
    private LinkedList<Character> decideN_W() {
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if(initDecide(knownStates,notVisited,false)){
            return new LinkedList<>();
        }
        boolean b;
        List<Node> list;
        // Tant qu'il reste des noeuds à explorer
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            for (Node n : list) {
                // Si on a trouvé un état qui correspond à l'objectif
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
                // Si l'état n'est pas encore connu
                if (b) {
                    // On l'ajoute aux listes d'états connus et à explorer
                    knownStates.add(n.treeState);
                    notVisited.add(notVisited.size(), n);
                }
            }
        }
        return new LinkedList<>();
    }

    // Exploration non informee en profondeur
    private LinkedList<Character> decideN_D() {
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if(initDecide(knownStates,notVisited,false)){
            return new LinkedList<>();
        }
        int i;
        boolean b;
        List<Node> list;
        // Tant qu'il reste des noeuds à explorer
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            i = 0;
            for (Node n : list) {
                // Si on a trouvé un état qui correspond à l'objectif
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
                // Si l'état n'est pas encore connu
                if (b) {
                    // On l'ajoute aux listes d'états connus et à explorer
                    knownStates.add(n.treeState);
                    // On ajout les noeuds au début de la liste des noeuds à explorer dans l'ordre dans lequel ils sont retournés par generateChildren
                    notVisited.add(i, n);
                    i++;
                }
            }
        }
        return new LinkedList<>();
    }

    // Exploration informée best first
    private LinkedList<Character> decideI_BF() {
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        if (initDecide(knownStates, notVisited, true)) {
            return new LinkedList<>();
        }
        boolean b;
        List<Node> list;
        // Tant qu'il reste des noeuds à explorer
        while (!notVisited.isEmpty()) {
            if (stopped) {
                return new LinkedList<>();
            }
            list = notVisited.remove(0).generateChildren();
            for (Node n : list) {
                // Si on a trouvé un état qui correspond à l'objectif
                if (desires(n.treeState)) {
                    return n.traceBack();
                }
                // Calcule la desirabilite de l'etat
                n.treeState.computeScore();
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
                        break;
                    }
                }
                // Si l'état n'est pas encore connu
                if (b) {
                    // On l'ajoute aux listes d'états connus et à explorer
                    knownStates.add(n.treeState);
                    notVisited.add(n);
                }
            }
            // On trie la liste des états à explorer pour mettre les états les plus désirables au début de la liste
            notVisited.sort(Comparator.comparingDouble(o -> o.treeState.score));
        }
        return new LinkedList<>();
    }

    public void sStop() {
        stopped = true;
    }
}