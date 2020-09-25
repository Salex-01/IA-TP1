import java.util.Arrays;

public class TreeState {
    int[][] map;
    int x;
    int y;
    double score;
    double electricityScore;

    public TreeState(int x1, int y1, int[][] map1, double sc) {
        x = x1;
        y = y1;
        map = map1;
        score = sc;
        electricityScore = sc;
    }

    public boolean equals(TreeState s) {
        if (s.x != x || s.y != y) {
            return false;
        }
        for (int i = 0; i < s.map.length; i++) {
            if (!Arrays.equals(s.map[i], map[i])) {
                return false;
            }
        }
        return true;
    }

    public void computeScore() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if ((map[i][j] & Constants.DUST) != 0) {
                    score += Constants.dustScore;
                }
                if ((map[i][j] & Constants.JEWEL) != 0) {
                    score += Constants.jewelScore;
                }
            }
        }
    }
}