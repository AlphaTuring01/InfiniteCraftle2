package lab.prog.infinitecraftle;

public class Score {
    private String date;
    private int score;

    public Score(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }
}
