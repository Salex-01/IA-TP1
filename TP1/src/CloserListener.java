import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// Listener pour fermer la fenêtre graphique

public class CloserListener implements WindowListener {
    Frame f;

    public CloserListener(Frame f1) {
        f = f1;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e1) {
        Main.e.sStop();
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