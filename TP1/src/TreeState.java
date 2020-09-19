import java.util.Arrays;

public class TreeState {
    int[][] map;
    int x;
    int y;

    public TreeState(int x1, int y1, int[][] map1) {
        x = x1;
        y = y1;
        map = map1;
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
}