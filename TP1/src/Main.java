import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Environment e = new Environment();
        boolean t = false;
        try {
            Frame f = new Frame();
            f.setBounds(0, 0, 1200, (int) 1200);
            Container c = new Container();
            f.add(c);
            c.setBounds(901, 0, 200, 200);
            Button b = new Button();
            c.add(b);
            b.setBounds(0, 0, 150, 150);
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

            Grid grid = new Grid(900, 900, e.height, e.width, e);
            grid.setBounds(0, 0, 900,900);
            f.add(grid);
            e.setGrid(grid);
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
}