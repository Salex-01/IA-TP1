import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
    static Grid grid;
    static Label label;
    static boolean t = false;
    static Environment e;
    static boolean showMap;
    static boolean stopped = false;
    static int time;
    static int bestLimit;
    static double bestScore;

    public static void main(String[] args) throws IOException, InterruptedException {
        e = new Environment();
        e.buildEnvironment(args);
        showMap = useInterface(args);
        time = setTimeLimit(args);
        try {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            Frame f = new Frame();
            f.setBounds((int) (dimension.width * 0.1), (int) (dimension.height * 0.1), (int) (dimension.width * 0.8), (int) (dimension.height * 0.8));
            Container c = new Container();
            f.add(c);
            c.setBounds(0, 0, dimension.width, dimension.height);
            Button b = new Button();
            c.add(b);
            b.setBounds((int) (dimension.width * 0.6), 100, (int) (dimension.width * 0.15), 100);
            b.setBackground(Color.RED);
            b.setForeground(Color.BLACK);
            b.setLabel("STOP");
            b.addActionListener(e1 -> {
                stopped = true;
                Main.e.sStop();
                f.dispose();
            });
            if (showMap) {
                grid = new Grid((int) (dimension.width * 0.05), (int) (dimension.height * 0.025), (int) (dimension.width * 0.5), (int) (dimension.height * 0.7), e.height, e.width);
                c.add(grid);
            } else {
                label = new Label();
                label.setBounds(0, 50, 300, 200);
                c.add(label);
            }
            f.addWindowListener(new CloserListener(f));
            f.setVisible(true);
        } catch (Exception e1) {
            t = true;
        }

        LinkedList<Pair<Integer, Double>> results = aggregate(runTest(args));

        for (Pair<Integer, Double> p : results) {
            System.out.println("limit " + p.key + " -> " + p.value);
        }
    }

    private static LinkedList<Pair<Integer, Double>> aggregate(LinkedList<Pair<Integer, Double>> rawResults) {
        LinkedList<Triplet<Integer, Double, Integer>> tmp = new LinkedList<>();
        boolean tem;
        while (!rawResults.isEmpty()) {
            Pair<Integer, Double> p = rawResults.remove(0);
            tem = true;
            for (Triplet<Integer, Double, Integer> p2 : tmp) {
                if (p.key == p2.first) {
                    p2.second += p.value;
                    p2.third++;
                    tem = false;
                    break;
                }
            }
            if (tem) {
                tmp.add(new Triplet<>(p.key, p.value, 1));
            }
        }
        LinkedList<Pair<Integer, Double>> results = new LinkedList<>();
        for (Triplet<Integer, Double, Integer> t : tmp) {
            results.add(new Pair<>(t.first, t.second / t.third));
        }
        return results;
    }

    private static LinkedList<Pair<Integer, Double>> runTest(String[] args) throws IOException {
        LinkedList<Pair<Integer, Double>> results = new LinkedList<>();
        LinkedList<Integer> testValues = new LinkedList<>();
        testValues.add(1);
        bestLimit = 0;
        bestScore = Integer.MAX_VALUE;
        while (!stopped && !testValues.isEmpty()) {
            e = new Environment();
            if (args.length != 0 && args.length % 2 == 0) {
                e.buildEnvironment(args);
            }
            int lim = testValues.remove(0);
            e.setLimit(lim);
            e.start();
            for (int i = 0; i < time; i++) {
                if (stopped) {
                    return results;
                }
                if (t && System.in.available() != 0) {
                    e.sStop();
                    return results;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            e.sStop();
            results.add(new Pair<>(lim, e.score));
            if (e.score <= (bestScore < 0 ? bestScore * 0.9 : bestScore * 1.1)) {
                if (lim > 1) {
                    testValues.add(lim - 1);
                }
                if (lim < e.map.length * e.map[0].length * 3) {
                    testValues.add(lim + 1);
                }
                if (e.score < bestScore) {
                    bestScore = e.score;
                    bestLimit = lim;
                }
            }
        }
        return results;
    }

    private static int setTimeLimit(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().contentEquals("time")) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return 10;
    }

    private static boolean useInterface(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().contentEquals("showmap")) {
                return args[i + 1].toLowerCase().startsWith("y");
            }
        }
        return true;
    }

    static synchronized void updateGraphics(boolean robotMoving) {
        if (showMap) {
            try {
                if (!t) {
                    grid.paint();
                    if (robotMoving) {
                        Thread.sleep(200);
                    }
                } else {
                    ConsoleGraphics.paint(e);
                    if (robotMoving) {
                        Thread.sleep(500);
                    }
                }
            } catch (NullPointerException | InterruptedException ignored) {
            }
        } else {
            if (!robotMoving) {
                try {
                    if (!t) {
                        label.setText("Best limit found : " + bestLimit + "\nScore : " + bestScore);
                    } else {
                        System.out.println("Best limit found : " + bestLimit + "\nScore : " + bestScore);
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }
    }
}