package lab.prog.infinitecraftle.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;


/**
 * Class representing the response to a login request.
 */
public class LoginResponse implements Serializable {
    private Element elementDay;
    private long initialTime;
    private String error;
    private ArrayList<String> listDates;
    private Game game;

    /**
     * Getter for the element of the day.
     * @return The element of the day.
     */
    public Element getElementDay() {
        return elementDay;
    }

    /**
     * Setter for the element of the day.
     * @param elementDay The element of the day.
     */
    public void setElementDay(Element elementDay) {
        this.elementDay = elementDay;
    }

    /**
     * Getter for the initial time.
     * @return The initial time.
     */
    public long getInitialTime() {
        return initialTime;
    }

    /**
     * Setter for the initial time.
     * @param initialTime The initial time.
     */
    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

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
     * Getter for the list of dates.
     * @return The list of dates.
     */
    public ArrayList<String> getListDates() {
        return listDates;
    }

    /**
     * Setter for the list of dates.
     * @param listDates The list of dates.
     */
    public void setListDates(ArrayList<Date> listDates) {
        ArrayList<String> arr = new ArrayList<>();
        for (Date date : listDates){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            arr.add(sdf.format(date));
        }
        this.listDates = arr;
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
