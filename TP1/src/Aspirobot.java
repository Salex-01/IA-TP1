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

    @Override
    public void run() {
        int intentionIndex;
        while (!stopped) {
            intentionIndex = 0;
            Main.updateGraphics(false);
            sensors.observe();
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
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                Main.updateGraphics(true);
                intentionIndex++;
                if (intentionIndex >= limit) {
                    break;
                }
            }
        }
        System.out.println("Stopped");
    }

    private LinkedList<Character> decideN_W() { //Exploration non informée en largeur
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        Node root = new Node(new TreeState(posX, posY, beliefs, 0), Constants.INIT, null);
        if (desires(root.treeState)) {
            return new LinkedList<>();
        }
        knownStates.add(root.treeState);
        notVisited.add(root);
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

    private LinkedList<Character> decideN_D() { //Exploration non informée en profondeur
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        Node root = new Node(new TreeState(posX, posY, beliefs, 0), Constants.INIT, null);
        if (desires(root.treeState)) {
            return new LinkedList<>();
        }
        knownStates.add(root.treeState);
        notVisited.add(root);
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
                    }
                }
                if (b) {
                    knownStates.add(n.treeState);
                    notVisited.add(0, n);
                }
            }
        }
        return new LinkedList<>();
    }

    private LinkedList<Character> decideI_BF() {    // Exploration informée best first
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        Node root = new Node(new TreeState(posX, posY, beliefs, 0), Constants.INIT, null);
        if (desires(root.treeState)) {
            return new LinkedList<>();
        }
        root.treeState.computeScore();
        knownStates.add(root.treeState);
        notVisited.add(root);
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
                n.treeState.computeScore(); // Calcul de la désirabilité de l'état
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
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