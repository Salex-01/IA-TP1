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

    @Override
    public void run() {
        while (!stopped) {
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
