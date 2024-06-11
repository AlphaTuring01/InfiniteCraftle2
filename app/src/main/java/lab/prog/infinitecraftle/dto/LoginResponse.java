package lab.prog.infinitecraftle.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;


public class LoginResponse {
    private Element elementDay;
    private long initialTime;
    private String error;
    private ArrayList<String> listDates;
    private Game game;

    public Element getElementDay() {
        return elementDay;
    }

    public void setElementDay(Element elementDay) {
        this.elementDay = elementDay;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<String> getListDates() {
        return listDates;
    }

    public void setListDates(ArrayList<Date> listDates) {
        ArrayList<String> arr = new ArrayList<>();
        for (Date date : listDates){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            arr.add(sdf.format(date));
        }
        this.listDates = arr;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
