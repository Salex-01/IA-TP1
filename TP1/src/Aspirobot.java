import java.util.LinkedList;
import java.util.Random;

public class Aspirobot extends Thread {
    Sensors sensors;
    Effectors effectors;
    int[][] beliefs;
    int posX = 0;
    int posY = 0;

    boolean stopped = false;

    public Aspirobot(Environment e) {
        sensors = new Sensors(e, this);
        effectors = new Effectors(e, this);
        this.start();
    }

    LinkedList<Character> decide() {

    }

    @Override
    public void run() {
        while (!stopped) {
            Random r;
            r = new Random();
            int rand = Math.abs(r.nextInt()) % 3;
            if (rand == 0) {
                int tmp = Math.abs(r.nextInt()) % 4;
                if (tmp == 0) {
                    if (this.posY > 0) {
                        this.effectors.move('u');
                    }
                }
                else if (tmp == 1) {
                    if (this.posY < sensors.e.height - 1) {
                        this.effectors.move('d');
                    }
                }
                else if (tmp == 2) {
                    if (this.posX > 0) {
                        this.effectors.move('l');
                    }
                }
                else {
                    if (this.posX < sensors.e.width - 1) {
                        this.effectors.move('r');
                    }
                }
                System.out.println("move : " + this.effectors.e.score + "\n");
                System.out.println("posX : " + this.posX + " posY : " + this.posY + "\n");
            }
            else if (rand == 1) {
                System.out.println("pick : " + this.effectors.e.score + "\n");
                this.effectors.pick();
            }
            else {
                System.out.println("suck : " + this.effectors.e.score + "\n");
                this.effectors.suck();
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
