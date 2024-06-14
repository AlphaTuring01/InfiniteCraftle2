package lab.prog.infinitecraftle;

/**
 * Class representing a score in the ranking table of the game.
 */
public class Score {
    private String date;
    private int score;

    /**
     * Constructor for Score.
     * @param date The date of the score.
     * @param score The score.
     */ 
    public Score(String date, int score) {
        this.date = date;
        this.score = score;
    }

    /**
     * Getter for the date.
     * @return The date of the score.
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for the score.
     * @return The score.
     */
    public int getScore() {
        return score;
    }
}
