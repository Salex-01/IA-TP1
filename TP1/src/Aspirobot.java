import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;

public class Aspirobot extends Thread {
    Sensors sensors;
    Effectors effectors;
    int posX = 0;
    int posY = 0;

    int[][] beliefs;

    Function<TreeState, Boolean> desires = state -> {
        for (int i = 0; i < state.map.length; i++) {
            for (int j = 0; j < state.map[0].length; j++) {
                if ((state.map[i][j] & Constants.DUST) != 0) {
                    return false;
                }
            }
        }
        return true;
    };

    boolean stopped = false;

    public Aspirobot(Environment e) {
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
                        break;
                    case Constants.PICK:
                        effectors.pick();
                        break;
                    default:
                        effectors.move(c);
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

    public void sStop() {
        stopped = true;
    }
}
