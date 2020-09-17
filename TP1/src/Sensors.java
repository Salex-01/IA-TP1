
public class Sensors {
    Aspirobot bot;
    Environment e;

    Sensors(Aspirobot bot, Environment e) {
        this.bot = bot;
        this.e = e;
    }

    public void look() {
        bot.beliefs = e.map.clone();
    }

    public int score() {
        return e.score;
    }
}