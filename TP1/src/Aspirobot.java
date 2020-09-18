import java.util.LinkedList;

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
                if ((state.map[i][j] & Constants.DUST) != 0) {
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
                switch (c) {
                    case Constants.SUCK:
                        effectors.suck();
                        System.out.println("suck at pos x = " + posX + " and y = " + posY + " score = " + e.score);
                        break;
                    case Constants.PICK:
                        effectors.pick();
                        System.out.println("pick at pos x = " + posX + " and y = " + posY + " score = " + e.score);
                        break;
                    default:
                        effectors.move(c);
                        System.out.println("move to x = " + posX + " and y = " + posY + " score = " + e.score);
                        break;
                }
            }
            try {
                Thread.sleep(1);
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
        while (!notVisited.isEmpty()) {
            for (Node n : notVisited.remove(0).generateChildren()) {
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

//    LinkedList<Character> decide() {
//        LinkedList<Character> intentions = new LinkedList<Character>();
//        TreeState treestate = new TreeState(posX, posY, beliefs);
//        Boolean b = desires(treestate);
//        if (b == false) {
//            LinkedList<Node> nodes = new LinkedList<Node>();
//            Node root = new Node(treestate, Constants.INIT, null);
//            nodes.add(root);
//            Node lastChild = null;
//            while (!nodes.isEmpty()) {
//
//                Node current = nodes.getFirst();
//                int xCurrent = root.treeState.x;
//                int yCurrent = root.treeState.y;
//
//                if (beliefs[xCurrent][yCurrent] == 0) {
//                    if (xCurrent > 0) {
//                        TreeState newTreeState = new TreeState(xCurrent - 1, yCurrent, current.treeState.map);
//                        Node child = new Node(newTreeState, Constants.LEFT, current);
//                        if (child.treeState.x == child.parent.treeState.x && child.treeState.y == child.parent.treeState.y) {
//                            nodes.add(child);
//                        }
//                    }
//                    if (xCurrent < e.width - 1) {
//                        TreeState newTreeState = new TreeState(xCurrent + 1, yCurrent, current.treeState.map);
//                        Node child = new Node(newTreeState, Constants.RIGHT, current);
//                        if (child.treeState.x == child.parent.treeState.x && child.treeState.y == child.parent.treeState.y) {
//                            nodes.add(child);
//                        }
//                    }
//                    if (yCurrent > 0) {
//                        TreeState newTreeState = new TreeState(xCurrent, yCurrent - 1, current.treeState.map);
//                        Node child = new Node(newTreeState, Constants.UP, current);
//                        if (child.treeState.x == child.parent.treeState.x && child.treeState.y == child.parent.treeState.y) {
//                            nodes.add(child);
//                        }
//                    }
//                    if (yCurrent < e.height - 1) {
//                        TreeState newTreeState = new TreeState(xCurrent, yCurrent + 1, current.treeState.map);
//                        Node child = new Node(newTreeState, Constants.DOWN, current);
//                        if (child.treeState.x == child.parent.treeState.x && child.treeState.y == child.parent.treeState.y) {
//                            nodes.add(child);
//                        }
//                    }
//
//                } else if (beliefs[xCurrent][yCurrent] == 1) {
//                    int[][] newMap = beliefs.clone();
//                    newMap[xCurrent][yCurrent] = 0;
//                    TreeState newTreeState = new TreeState(xCurrent, yCurrent, newMap);
//                    Node child = new Node(newTreeState, Constants.SUCK, current);
//                    nodes.add(child);
//                    if (desires.apply(newTreeState) == true) {
//                        lastChild = child;
//                        nodes.clear();
//                    }
//
//                } else if (beliefs[xCurrent][yCurrent] == 2) {
//                    int[][] newMap = beliefs.clone();
//                    newMap[xCurrent][yCurrent] = 0;
//                    TreeState newTreeState = new TreeState(xCurrent, yCurrent, newMap);
//                    Node child = new Node(newTreeState, Constants.PICK, current);
//                    nodes.add(child);
//                    if (desires.apply(newTreeState) == true) {
//                        lastChild = child;
//                        nodes.clear();
//                    }
//                } else {
//                    int[][] newMap1 = beliefs.clone();
//                    int[][] newMap2 = beliefs.clone();
//                    newMap1[xCurrent][yCurrent] = 0;
//                    newMap2[xCurrent][yCurrent] = 0;
//                    TreeState newTreeState1 = new TreeState(xCurrent, yCurrent, newMap1);
//                    TreeState newTreeState2 = new TreeState(xCurrent, yCurrent, newMap2);
//                    Node child1 = new Node(newTreeState1, Constants.PICK, current);
//                    Node child2 = new Node(newTreeState2, Constants.PICK, child1);
//                    nodes.add(child2);
//                    if (desires.apply(newTreeState2) == true) {
//                        lastChild = child2;
//                        nodes.clear();
//                    }
//                }
//            }
//            while (lastChild != null) {
//                intentions.add(lastChild.transition);
//                lastChild = lastChild.parent;
//            }
//        }
//        return intentions;
//    }
}
