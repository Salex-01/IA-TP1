import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CloserListener implements WindowListener {
    Environment e;
    Frame f;

    public CloserListener(Environment e1, Frame f1) {
        e = e1;
        f = f1;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e1) {
        e.sStop();
        f.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
