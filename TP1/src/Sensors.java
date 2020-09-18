public class Sensors {
    Aspirobot bot;
    Environment e;

    Sensors(Environment e, Aspirobot bot) {
        this.bot = bot;
        this.e = e;
    }

    public void look() {
        bot.beliefs = e.map.clone();
    }

    public double score() {
        return e.score;
    }
}