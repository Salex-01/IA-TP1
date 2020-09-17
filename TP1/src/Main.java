import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Environment e = new Environment();
        Frame f = new Frame();
        f.setBounds(0, 0, 200, (int) 200);
        Container c = new Container();
        f.add(c);
        c.setBounds(0, 0, 200, 200);
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
        f.addWindowListener(new CloserListener(e,f));
        f.setVisible(true);
        if (args.length != 0 && args.length % 2 == 0) {
            e.parseArgs(args);
        }
        e.start();
    }
}
