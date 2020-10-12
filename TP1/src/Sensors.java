public class Sensors {
    Aspirobot bot;
    Environment e;

    Sensors(Environment e, Aspirobot bot) {
        this.bot = bot;
        this.e = e;
    }
    /*
    récupère la carte du manoir de l'environement pour la copier dans "beliefs" de l'aspirobot
     */
    public void observe() {
        bot.beliefs = Constants.deepClone(e.map);
    }
    /*
    renvoi le score de l'aspirobot
     */
    public double score() {
        return e.score;
    }
}