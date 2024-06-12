package lab.prog.infinitecraftle.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Game class that represents a game played by the user.
 * Contains the game's score, time, win status, elements, date and user
 */
public class Game {
    private int score;
    private int timeMillis;
    private boolean win;
    private ArrayList<Element> elements;
    private String date;
    private User user;
    private Element targetElement;
    /**
     * Constructor for Game class
     * @param date date of the game
     * @param user user that played the game
     */
    public Game(Date date, User user) {
        this.date = DateToString(date);
        this.user = user;
        this.score = 0;
        this.timeMillis = 0;
        this.win = false;
        this.elements = new ArrayList<>();
        this.targetElement = new Element();
    }

    public Game(ArrayList<Element> elements){
        this.elements = elements;
    }
    String DateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    Date StringToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * Getter for target element of the game
     * @return target element of the game
     */
    public Element getTargetElement() {
        return targetElement;
    }
    /**
     * Getter for date of the game
     * @return date of the game
     */
    public Date getDate() {
        return StringToDate(date);
    }
    public String getDateString() {
        return date;
    }

    /**
     * Getter for user that played the game
     * @return user that played the game
     */
    public User getUser() {
        return user;
    }

    /**
     * Getter for score of the game
     * @return score of the game
     */
    public int getScore() {
        return score;
    }
    /**
     * Setter for target element of the game
     * @param targetElement target element of the game
     */
    public void setTargetElement(Element targetElement) {
        this.targetElement = targetElement;
    }
    /**
     * Setter for score of the game
     * @param score score of the game
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for time of the game in milliseconds
     * @return time of the game in milliseconds
     */
    public int getTimeMillis() {
        return timeMillis;
    }

    /**
     * Setter for time of the game in milliseconds
     * @param timeMillis time of the game in milliseconds
     */
    public void setTimeMillis(int timeMillis) {
        this.timeMillis = timeMillis;
    }

    /**
     * Getter for win status of the game
     * @return win status of the game
     */
    public boolean isWin() {
        return win;
    }

    /**
     * Setter for win status of the game
     * @param win win status of the game
     */
    public void setWin(boolean win) {
        this.win = win;
    }

    /**
     * Getter for elements of the game
     * @return list of elements of the game
     */
    public ArrayList<Element> getElements() {
        return elements;
    }

    /**
     * Setter for elements of the game
     * @param elements elements of the game
     */
    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    /**
     * Method to set game end details
     *
     * @param score score of the game
     * @param time time of the game in milliseconds
     * @param win win status of the game
     */
    public void setEndGame(int score, int time, boolean win) {
        setScore(score);
        setTimeMillis(time);
        setWin(win);
    }
}