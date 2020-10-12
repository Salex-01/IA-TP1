import java.util.Arrays;

public class TreeState {
    int[][] map;
    int x;
    int y;
    double score;
    double electricityScore;
    /*
    Cree un etat de l'arbre compose de l'etat du manoir, de la position de l'aspirobot et des scores
     */
    public TreeState(int x1, int y1, int[][] map1, double sc) {
        x = x1;
        y = y1;
        map = map1;
        score = sc;
        electricityScore = sc;
    }

    /*
    teste l'egalite entre deux etat de l'arbre
     */
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
    /*
    ajoute à score le cout de la poussière et des bijoux restants
     */
    public void computeScore() {
        for (int[] col : map) {
            for (int j = 0; j < map[0].length; j++) {
                if ((col[j] & Constants.DUST) != 0) {
                    score += Constants.dustScore;
                }
                if ((col[j] & Constants.JEWEL) != 0) {
                    score += Constants.jewelScore;
                }
            }
        }
    }
}