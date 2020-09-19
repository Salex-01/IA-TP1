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
            sensors.look();
            intentions = decide();
            for (char c : intentions) {
                System.out.println(posX + " " + posY + " : " + c);
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
            }
            System.out.println("score : " + sensors.score());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("Stopped");
    }

    private LinkedList<Character> decide() {
        LinkedList<TreeState> knownStates = new LinkedList<>();
        LinkedList<Node> notVisited = new LinkedList<>();
        Node root = new Node(new TreeState(posX, posY, beliefs), Constants.INIT, null);
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
                b = true;
                for (TreeState ts : knownStates) {
                    if (n.treeState.equals(ts)) {
                        b = false;
                    }
                }
                if (b) {
                    knownStates.add(n.treeState);
                    notVisited.add(n);
                    n.parent.children.add(n);
                    if (desires(n.treeState)) {
                        return n.traceBack();
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    public void sStop() {
        stopped = true;
    }
}