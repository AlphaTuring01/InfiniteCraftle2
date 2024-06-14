package lab.prog.infinitecraftle.dto;
import lab.prog.infinitecraftle.domain.Game;

/**
 * Class representing the response to a change date request.
 */
public class ChangeDateResponse {
    private String error;
    private Game game;
    /**
     * Getter for the error message.
     * @return The error message.
     */
    public String getError() {
        return error;
    }
    /**
     * Setter for the error message.
     * @param error The error message.
     */
    public void setError(String error) {
        this.error = error;
    }
    /**
     * Getter for the game.
     * @return The game.
     */
    public Game getGame() {
        return game;
    }
    /**
     * Setter for the game.
     * @param game The game.
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
