public class Effectors {
    Environment e;
    Aspirobot bot;

    public Effectors(Environment e1, Aspirobot b) {
        e = e1;
        bot = b;
    }

    // Fait se déplacer l'Aspirobot dans la direction d donnée en argument et augmente le score du coût de l'électricité
    void move(char d) {
        e.score += Constants.electricityCost;
        switch (d) {
            case 'u':
                bot.posY--;
                break;
            case 'd':
                bot.posY++;
                break;
            case 'r':
                bot.posX++;
                break;
            case 'l':
                bot.posX--;
                break;
        }
    }

    // Récupère le bijou se trouvant à la position de l'Aspiriobot et modifie le score en conséquence
    void pick() {
        e.score += Constants.electricityCost;
        if ((e.map[bot.posX][bot.posY] & Constants.JEWEL) != 0) {
            e.map[bot.posX][bot.posY] &= (~Constants.JEWEL);
            e.score -= Constants.jewelBonus;
        }
    }

    // Aspire la poussière et le bijou se trouvant à la position de l'Aspirobot et modifie le score en conséquence
    void suck() {
        e.score += Constants.electricityCost;
        if ((e.map[bot.posX][bot.posY] & Constants.JEWEL) != 0) {
            e.score += Constants.jewelCost;
            e.map[bot.posX][bot.posY] &= (~Constants.JEWEL);
        }
        if ((e.map[bot.posX][bot.posY] & Constants.DUST) != 0) {
            e.map[bot.posX][bot.posY] &= (~Constants.DUST);
            e.score -= Constants.dustBonus;
        }
    }
}