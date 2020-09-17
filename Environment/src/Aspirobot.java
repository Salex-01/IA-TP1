public class Aspirobot extends Thread {
    Sensors sensors;
    Effectors effectors;
    int[][] beliefs;

    boolean stopped = false;

    public Aspirobot(Environment e) {
        sensors = new Sensors(e);
        effectors = new Effectors(e);
        this.start();
    }

    @Override
    public void run() {
        while (!stopped) {

        }
    }

    public void sStop() {
        stopped = true;
    }
}
