public class Constants {
    static final int DUST = 1;
    static final int JEWEL = 2;

    static double electricityCost = 1;
    static double jewelCost = 1000;

    static final char UP = 'u';
    static final char DOWN = 'd';
    static final char RIGHT = 'r';
    static final char LEFT = 'l';
    static final char PICK = 'p';
    static final char SUCK = 's';
    static final char INIT = 'i';

    // Copie une matrice d'entiers à 2 dimensions
    static int[][] deepClone(int[][] map) {
        int[][] res = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, res[i], 0, map[0].length);
        }
        return res;
    }

    static double dustScore = 1;
    static double jewelScore = 1;

    static double dustBonus = 5;
    static double jewelBonus = 2;
}