import java.util.Random;

public class Environment extends Thread {
    int height = 5;
    int width = 5;
    int[][] map;
    Aspirobot bot;
    double score = 0;
    double pDust = 0.15;
    double pJewel = 0.05;
    Random r = new Random();

    String mode = "n_w";
    int limit;

    boolean stopped = false;

    void buildEnvironment(String[] args) {
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
                case "mode":
                    mode = args[i + 1].toLowerCase();
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
        bot = new Aspirobot(this, mode, limit);
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
                Thread.sleep(10);
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
    }

    public void sStop() {
        stopped = true;
    }

    public void setLimit(int intentionsLimit) {
        limit = intentionsLimit;
    }
}