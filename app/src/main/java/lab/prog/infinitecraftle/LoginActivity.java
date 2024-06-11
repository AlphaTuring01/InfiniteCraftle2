package lab.prog.infinitecraftle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        User user = new User(username, password);

        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance(this);
            int code = databaseManager.authenticateUser(user);
            if (code != 0) {
                Toast.makeText(this, ErrorCodeDictionary.getErrorMessage(code), Toast.LENGTH_SHORT).show();
                return;
            } else {
                Game game = new Game(new Date(), user);
                databaseManager.updateLastGames(game.getDate());
                int code2 = databaseManager.getGame(game);
                if (code2 != 0) {
                    Toast.makeText(this, ErrorCodeDictionary.getErrorMessage(code2), Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("initialTime", System.currentTimeMillis());
                editor.putString("user", username);
                editor.putString("elementDay", databaseManager.getElementDay(new Date()).toString());
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("game", game);
                intent.putExtra("listDates", databaseManager.getDates());
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Error during login", e);
            Toast.makeText(this, ErrorCodeDictionary.getErrorMessage(6), Toast.LENGTH_SHORT).show();
        }
    }
}
