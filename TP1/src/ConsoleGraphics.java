import java.io.PrintStream;

public class ConsoleGraphics {
    public static void paint(Environment e) {
        PrintStream ps = System.out;
        ps.println("\n\n\n\n");
        int x = e.bot.posX;
        int y = e.bot.posY;
        StringBuilder sb = new StringBuilder("#");
        int j;
        for (j = 0; j < e.width; j++) {
            sb.append("####");
        }
        for (int i = 0; i < e.height; i++) {
            sb.append("\n#");
            for (j = 0; j < e.width; j++) {
                sb.append((x == j && y == i) ? "BOT#" : "   #");
            }
            sb.append("\n#");
            for (j = 0; j < e.width; j++) {
                sb.append(((e.map[j][i] & Constants.DUST) != 0) ? "D " : "  ");
                sb.append(((e.map[j][i] & Constants.JEWEL) != 0) ? "J#" : " #");
            }
            sb.append("\n#");
            for (j = 0; j < e.width; j++) {
                sb.append("####");
            }
        }
        ps.println(sb.toString());
    }
}