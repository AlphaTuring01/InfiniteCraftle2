package lab.prog.infinitecraftle;
import android.content.SharedPreferences;
import android.content.Context;

import lab.prog.infinitecraftle.domain.User;


/**
 * Class for handling shared preferences.
 */
public class SharedPreferencesHandler {
    private static final String PREF_NAME = "ICPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_GAME_DATE = "gameDate";
   
    /**
     * Getter for the user.
     * @param context The context.
     * @return
     */
    public User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String idString = sharedPreferences.getString(KEY_USER_ID, null);
        String username = sharedPreferences.getString(KEY_USER_NAME, null);
        String password = sharedPreferences.getString(KEY_USER_PASSWORD, null);
        if(idString == null || username == null || password == null) return null;
        User user = new User(username, password);
        user.setId(Integer.parseInt(idString));
        return user;
    }

    /**
     * Setter for the game date.
     * @param context The context.
     * @param date The date.
     */
    public void setGameDate(Context context, String date) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GAME_DATE, date);
        editor.apply();
    }

    /**
     * Getter for the game date.
     * @param context The context.
     * @return The game date.
     */
    public String getGameDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_GAME_DATE, null);
    }

    /**
     * Setter for the user.
     * @param context The context.
     * @param user The user.
     * @param gameDate The game date.
     */
    public void saveUserData(Context context, User user, String gameDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, String.valueOf(user.getId()));
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_GAME_DATE, gameDate);
        editor.apply();
    }

    /**
     * Method to clear the user data.
     * @param context The context.
     */
    public void clearUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
