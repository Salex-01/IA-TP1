public class Aspirobot extends Thread {
    Sensors sensors;
    Effectors effectors;
    int[][] beliefs;
    int posX = 0;
    int posY = 0;

    boolean stopped = false;

    public Aspirobot(Environment e) {
        sensors = new Sensors(this, e);
        effectors = new Effectors(e,this);
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
