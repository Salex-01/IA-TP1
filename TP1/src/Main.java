import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Environment e = new Environment();
        Frame f = new Frame();
        f.setBounds(0, 0, 100, (int) 100);
        Container c = new Container();
        f.add(c);
        c.setBounds(0, 0, 100, 100);
        Button b = new Button();
        c.add(b);
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
        e.start();
    }
}
