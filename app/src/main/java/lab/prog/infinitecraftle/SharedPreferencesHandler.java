package lab.prog.infinitecraftle;
import android.content.SharedPreferences;
import android.content.Context;
public class SharedPreferencesHandler {
    private static final String PREF_NAME = "ICPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_GAME_DATE = "gameDate";
    public String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getGameDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_GAME_DATE, null);
    }
    public void saveUserData(Context context, String userId, String gameDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_GAME_DATE, gameDate);
        editor.apply();
    }
    public void clearUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
