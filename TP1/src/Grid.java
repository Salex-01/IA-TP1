import java.awt.*;

class Grid extends Canvas {
    int width;
    int height;
    int rows;
    int cols;

    Grid(int posx, int posy, int w, int h, int r, int c) {
        width = w;
        height = h;
        rows = r;
        cols = c;
        this.setBounds(posx, posy, w, h);
    }

    public void paint() {
        Graphics g = this.getGraphics();
        int i, j;
        g.clearRect(0, 0, width, height);
        // draw the rows
        double rowHeight = ((double) height / rows) * 0.95;
        for (i = 0; i <= rows; i++)
            g.drawLine((int) (width * 0.025), (int) (i * rowHeight + height * 0.025), (int) (width * 0.975), (int) (i * rowHeight + height * 0.025));

        // draw the columns
        double colWidth = ((double) width / cols) * 0.95;
        for (i = 0; i <= cols; i++)
            g.drawLine((int) (i * colWidth + width * 0.025), (int) (height * 0.025), (int) (i * colWidth + width * 0.025), (int) (height * 0.975));
        // draw the dusts and jewels
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
                if ((Main.e.map[i][j] & Constants.DUST) != 0) {
                    g.drawString("DUST", (int) ((i + 0.1) * colWidth + width * 0.025), (int) ((j + 0.67) * rowHeight + height * 0.025));
                } else if ((Main.e.map[i][j] & Constants.JEWEL) != 0) {
                    g.drawString("JEWEL", (int) ((i + 0.55) * colWidth + width * 0.025), (int) ((j + 0.67) * rowHeight + height * 0.025));
                }
            }
        }
        // draw the Aspirobot
        g.drawString("ASPIROBOT", (int) ((Main.e.bot.posX + 0.33) * colWidth + width * 0.025), (int) ((Main.e.bot.posY + 0.25) * rowHeight + height * 0.025));
    }
}