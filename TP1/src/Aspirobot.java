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
    String decideMode = "n_w";

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

    boolean stopped = false;

    public Aspirobot(Environment e) {
        this.e = e;
        sensors = new Sensors(e, this);
        effectors = new Effectors(e, this);
        this.start();
    }

    @Override
    public void run() {
        LinkedList<Character> intentions;
        while (!stopped) {
            sensors.observe();
            switch (decideMode) {
                case "n_w":
                    intentions = decideN_W();
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
                System.out.println(posX + " " + posY + " : " + c);
                switch (c) {
                    case Constants.SUCK:
                        effectors.suck();
                        e.grid.paint(e.grid.getGraphics());
                        break;
                    case Constants.PICK:
                        effectors.pick();
                        e.grid.paint(e.grid.getGraphics());
                        break;
                    default:
                        effectors.move(c);
                        e.grid.paint(e.grid.getGraphics());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                        break;
                }
            }
            System.out.println("score : " + sensors.score());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("Stopped");
    }

    private LinkedList<Character> decideN_W() {
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
                    notVisited.add(n);
                }
            }
        }
        return new LinkedList<>();
    }

    private LinkedList<Character> decideI_BF() {
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
                n.treeState.computeScore();
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