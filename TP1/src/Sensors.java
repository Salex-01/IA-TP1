public class Sensors {
    Aspirobot bot;
    Environment e;

    Sensors(Environment e, Aspirobot bot) {
        this.bot = bot;
        this.e = e;
    }

    public void observe() {
        bot.beliefs = Constants.deepClone(e.map);
    }

    public double score() {
        return e.score;
    }
}