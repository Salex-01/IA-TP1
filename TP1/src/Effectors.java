public class Effectors {
    Environment e;
    Aspirobot bot;

    public Effectors(Environment e1, Aspirobot b) {
        e = e1;
        bot = b;
    }

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

    void pick() {
        e.score += Constants.electricityCost;
        e.map[bot.posY][bot.posX] &= (~Constants.JEWEL);
    }

    void suck() {
        e.score += Constants.electricityCost;
        if ((e.map[bot.posY][bot.posX] & Constants.JEWEL) != 0) {
            e.score += Constants.jewelCost;
            e.map[bot.posY][bot.posX] &= (~Constants.JEWEL);
        }
        e.map[bot.posY][bot.posX] &= (~Constants.DUST);
    }
}
