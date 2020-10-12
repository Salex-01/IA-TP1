import java.awt.*;
import java.io.IOException;
import java.util.*;

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
    static int nRuns;
    static int lim;

    public static void main(String[] args) throws IOException {
        // creer l'environnement et recupère les arguments
        e = new Environment(args);
        showMap = useInterface(args);
        time = setTimeLimit(args);
        nRuns = setNRuns(args);
        try {
            // met en place l'affichage
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
                label.setBounds(0, 50, 500, 200);
                c.add(label);
            }
            f.addWindowListener(new CloserListener(f));
            f.setVisible(true);
        } catch (Exception e1) {
            t = true;
        }
        // lance les testes
        Set<Map.Entry<Integer, Pair<Double, Integer>>> s = runTest(args).entrySet();

        for (Map.Entry<Integer, Pair<Double, Integer>> e : s) {
            System.out.println("limit " + e.getKey() + " -> " + (e.getValue().key / e.getValue().value));
        }
    }

    @SuppressWarnings(value = "BusyWait")
    private static HashMap<Integer, Pair<Double, Integer>> runTest(String[] args) throws IOException {
        HashMap<Integer, Pair<Double, Integer>> results = new HashMap<>();
        // stock les valeurs de limite à tester
        LinkedList<Integer> testValues = new LinkedList<>();
        testValues.add(1);
        testValues.add(e.map.length * e.map[0].length * 3);
        // initialise la meilleur limite et le meilleur score
        bestLimit = 0;
        bestScore = Double.MAX_VALUE;
        //tant qu'il y a une valeur de limite à tester
        while (!stopped && !testValues.isEmpty()) {
            lim = testValues.remove(0);
            double score = 0;
            /*
            On creer un environnement pour tester la limite actuel, se teste sera realise nRuns fois
             */
            for (int j = 0; j < nRuns; j++) {
                e = new Environment(args);
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
                score += e.score;
            }
            // on fait la moyenne des nRuns scores et on crée une paire avec la limite testee
            double scorePerRun = score / nRuns;
            Pair<Double, Integer> p = results.get(lim);
            if (p == null) {
                p = new Pair<>(scorePerRun, 1);
            } else {
                p.key += scorePerRun;
                p.value++;
            }
            results.put(lim, p);
            if (lim == bestLimit) {
                bestScore = p.key / p.value;
            }
            Set<Map.Entry<Integer, Pair<Double, Integer>>> s = results.entrySet();
            for (Map.Entry<Integer, Pair<Double, Integer>> e : s) {
                double tmp = e.getValue().key / e.getValue().value;
                // met à jour le meilleur score et la meilleur limite
                if (tmp < bestScore) {
                    bestScore = tmp;
                    bestLimit = e.getKey();
                }
            }
            // ajoute lim - 1 et lim + 1 aux limites à tester si cela est possible
            if (p.key / p.value <= (bestScore < 0 ? bestScore * 0.9 : bestScore * 1.1)) {
                if (lim > 1) {
                    testValues.add(lim - 1);
                }
                if (lim < e.map.length * e.map[0].length * 3) {
                    testValues.add(lim + 1);
                }
            }
        }
        return results;
    }

    /*
    modifie l'attribut NRuns avec celui passe en argument
     */
    private static int setNRuns(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].toLowerCase().contentEquals("runs")) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return 10;
    }

    /*
    modifie l'attribut time avec celui passe en argument
     */
    private static int setTimeLimit(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().contentEquals("time")) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return 10;
    }

    /*
    modifie l'attribut showmap avec celui passe en argument
     */
    private static boolean useInterface(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().contentEquals("showmap")) {
                return args[i + 1].toLowerCase().startsWith("y");
            }
        }
        return true;
    }

    /*
    met à jour l'affichage
     */
    static synchronized void updateGraphics(boolean pause, boolean updateDuringBenchmark) {
        if (showMap) {
            try {
                if (!t) {
                    grid.paint();
                    if (pause) {
                        Thread.sleep(200);
                    }
                } else {
                    ConsoleGraphics.paint(e);
                    if (pause) {
                        Thread.sleep(500);
                    }
                }
            } catch (NullPointerException | InterruptedException ignored) {
            }
        } else {
            if (updateDuringBenchmark) {
                try {
                    if (!t) {
                        label.setText("Best limit found : " + bestLimit + " - Score : " + bestScore + " - Testing : " + lim);
                    } else {
                        System.out.println("Best limit found : " + bestLimit + " - Score : " + bestScore + " - Testing : " + lim);
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }
    }
}