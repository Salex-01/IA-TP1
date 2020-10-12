public class Effectors {
    Environment e;
    Aspirobot bot;

    public Effectors(Environment e1, Aspirobot b) {
        e = e1;
        bot = b;
    }

    /*
    fait bouger l'aspirobot dans la direction d donnee en argument et augment le score du coup electrique
     */
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

    /*
    recupere le bijou se trouvant à la position de l'aspiriobot et modifie le score
     */
    void pick() {
        e.score += Constants.electricityCost;
        if ((e.map[bot.posX][bot.posY] & Constants.JEWEL) != 0) {
            e.map[bot.posX][bot.posY] &= (~Constants.JEWEL);
            e.score -= Constants.jewelBonus;
        }
    }

    /*
    aspire la poussiere se trouvant à la position de l'aspiriobot et modifie le score
     */
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