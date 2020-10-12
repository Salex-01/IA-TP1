import java.util.Random;

public class Environment extends Thread {
    int height = 5;
    int width = 5;
    final int[][] map;
    Aspirobot bot;
    double score = 0;
    double pDust = 0.15;
    double pJewel = 0.05;
    Random r = new Random();

    String mode = "n_w";
    int limit;

    boolean stopped = false;

    /*
    cree un environement suivant les parametres envoyes en argument
     */
    public Environment(String[] args){
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i].toLowerCase()) {
                case "h":
                    height = Integer.parseInt(args[i + 1]);
                    break;
                case "w":
                    width = Integer.parseInt(args[i + 1]);
                    break;
                case "d":
                    pDust = Double.parseDouble(args[i + 1]);
                    break;
                case "j":
                    pJewel = Double.parseDouble(args[i + 1]);
                    break;
                case "ec":
                    Constants.electricityCost = Double.parseDouble(args[i + 1]);
                    break;
                case "jc":
                    Constants.jewelCost = Double.parseDouble(args[i + 1]);
                    break;
                case "ds":
                    Constants.dustScore = Double.parseDouble(args[i + 1]);
                    break;
                case "js":
                    Constants.jewelScore = Double.parseDouble(args[i + 1]);
                    break;
                case "db":
                    Constants.dustBonus = Double.parseDouble(args[i + 1]);
                    break;
                case "jb":
                    Constants.jewelBonus = Double.parseDouble(args[i + 1]);
                    break;
                case "mode":
                    mode = args[i + 1].toLowerCase();
                    break;
                case "time":
                case "showmap":
                case "runs":
                    break;
                default:
                    System.out.println("Unknown argument");
                    System.exit(1);
                    break;
            }
        }
        map = new int[width][height];
    }

    /*
    boucle d'execution de l'environement
     */
    @Override
    @SuppressWarnings(value = "BusyWait")
    public void run() {
        // à chaque nouvelle environement on cree un Aspirobot avec un environement un mode et une limite
        bot = new Aspirobot(this, mode, limit);
        while (!stopped) {
            synchronized (map) {
                double d = Math.abs(r.nextDouble()) % 1.0;
                // genere de la poussiere avec la probabilite pDust
                if (d < pDust) {
                    generate(map, Constants.DUST);
                }
                d = Math.abs(r.nextDouble()) % 1.0;
                // genere un bijou avec la probabilite pJewel
                if (d < pJewel) {
                    generate(map, Constants.JEWEL);
                }
                // met à jour l'affichage
                Main.updateGraphics(true, true);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        // stop le robot si l'environement est stoppe
        bot.sStop();
    }

    // permet de generer sur la carte du manoir l'element type en argument
    private void generate(int[][] map, int type) {
        int x;
        int y;
        do {
            if (stopped) {
                return;
            }
            x = Math.abs(r.nextInt()) % width;
            y = Math.abs(r.nextInt()) % height;
        } while ((map[x][y] & type) != 0);
        map[x][y] |= type;
    }

    public void sStop() {
        stopped = true;
    }

    public void setLimit(int intentionsLimit) {
        limit = intentionsLimit;
    }
}