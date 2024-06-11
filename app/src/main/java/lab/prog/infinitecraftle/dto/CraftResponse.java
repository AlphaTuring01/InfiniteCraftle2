package lab.prog.infinitecraftle.dto;

import lab.prog.infinitecraftle.domain.Game;
import lab.prog.infinitecraftle.domain.Element;

/**
 * Response class is used to send the response to the client.
 * It contains the error message, the game, the element and a boolean that indicates if the element was crafted.
 * The error message is used to inform the client if an error occurred.
 * The game is used to update the game in the session.
 * The element is used to inform the client the element crafted.
 * The boolean is used to inform the client if the element was crafted.
 */
public class CraftResponse {
    private String error;
    private Game game;
    private Element element;
    private boolean crafted;

    /**
     * Getter for error message
     * @return error message
     */
    public String getError() {
        return error;
    }

    /**
     * Setter for error message
     * @param error error message
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Getter for game
     * @return game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setter for game
     * @param game game object
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Getter for element
     * @return element object
     */
    public Element getElement() {
        return element;
    }

    /**
     * Setter for element
     * @param element element object
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Getter for crafted
     * @return boolean that indicates if the element was crafted
     */
    public boolean isCrafted() {
        return crafted;
    }

    /**
     * Setter for crafted
     * @param crafted boolean that indicates if the element was crafted
     */
    public void setCrafted(boolean crafted) {
        this.crafted = crafted;
    }
}
