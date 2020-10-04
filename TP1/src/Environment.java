import java.util.Random;

public class Environment extends Thread {
    int height = 5;
    int width = 5;
    int[][] map;
    Aspirobot bot;
    double score = 0;
    double pDust = 0.3;
    double pJewel = 0.1;
    Random r = new Random();

    String mode = "n_w";

    boolean stopped = false;

    void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
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
                case "mode":
                    mode = args[i + 1];
                default:
                    System.out.println("Unknown argument");
                    System.exit(1);
                    break;
            }
        }
    }

    @Override
    public void run() {
        map = new int[width][height];
        bot = new Aspirobot(this, mode);
        while (!stopped) {
            synchronized (map) {
                double d = Math.abs(r.nextDouble()) % 1.0;
                if (d < pDust) {
                    generate(map, Constants.DUST);
                }
                d = Math.abs(r.nextDouble()) % 1.0;
                if (d < pJewel) {
                    generate(map, Constants.JEWEL);
                }
                Main.updateGraphics(false);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        bot.sStop();
    }

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
        System.out.println("généré " + (type == Constants.DUST ? "DUST" : "JEWEL") + " en " + x + " " + y);
    }

    public void sStop() {
        stopped = true;
    }
}