package lab.prog.infinitecraftle.domain;

import java.io.Serializable;


/**
 * Class representing a row in the ranking table of the game for serialization.
 */
public class RankingRow implements Serializable {
    private String username;
    private int score;
    /**
     * Getter for the username.
     * 
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username.
     * 
     * @param username The username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the score.
     * 
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for the score.
     * 
     * @param score The score of the player.
     */
    public void setScore(int score) {
        this.score = score;
    }
}
