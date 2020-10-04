import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    static Grid grid;
    static boolean t = false;
    static Environment e = new Environment();

    public static void main(String[] args) throws IOException, InterruptedException {
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
                e.sStop();
                f.dispose();
            });

            if (args.length != 0 && args.length % 2 == 0) {
                e.parseArgs(args);
            }
            grid = new Grid((int) (dimension.width * 0.05), (int) (dimension.height * 0.025), (int) (dimension.width * 0.5), (int) (dimension.height * 0.7), e.height, e.width, e);
            c.add(grid);
            f.addWindowListener(new CloserListener(e, f));
            f.setVisible(true);
        } catch (Exception e1) {
            t = true;
        }

        e.start();
        if (t) {
            while (System.in.available() == 0) {
                Thread.sleep(100);
            }
            e.sStop();
        }
    }

    static synchronized void updateGraphics(boolean robotMoving) {
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
    }
}