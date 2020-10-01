import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Grid extends Canvas {
    int width;

    int height;

    int rows;

    int cols;

    Environment e;

    Grid(int w, int h, int r, int c, Environment e) {
        width = w;
        height = h;
        rows = r;
        cols = c;
        this.e = e;

    }

    public void paint(Graphics g) {
        int i, j;
        g.clearRect(0,0,width,height);
        // draw the rows
        int rowHeight = height / (rows);
        for (i = 0; i < rows; i++)
            g.drawLine(0, i * rowHeight, width, i * rowHeight);

        // draw the columns
        int rowWidth = width / (cols);
        for (i = 0; i < cols; i++)
            g.drawLine(i * rowWidth, 0, i * rowWidth, height);

        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
                if (e.map[i][j] == 1) {
                    g.drawString("DUST",i * rowWidth + rowWidth/2 ,j*rowHeight + rowHeight/2);
                } else if(e.map[i][j] == 2) {
                    g.drawString("JEWEL",i * rowWidth + rowWidth/2 ,j*rowHeight + rowHeight/2);
                }
                else if (e.map[i][j] == 3) {
                    g.drawString("DUST + JEWEL",i * rowWidth + rowWidth/2 ,j*rowHeight + rowHeight/2);
                }
            }
        }
        g.drawString("ASPIROBOT", e.bot.posX * rowWidth + rowWidth/2, e.bot.posY * rowHeight + rowHeight/4);
    }
}
