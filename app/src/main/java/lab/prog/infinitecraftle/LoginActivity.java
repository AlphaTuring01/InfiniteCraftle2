package lab.prog.infinitecraftle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import lab.prog.infinitecraftle.domain.User;
import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferencesHandler preferencesHandler = new SharedPreferencesHandler();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        EditText usernameEditText = findViewById(R.id.loginEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView errorText = findViewById(R.id.errorText);
        TextView signupClickable = findViewById(R.id.signupClickable);

        User user = preferencesHandler.getUser(this);
        String gameDate = preferencesHandler.getGameDate(this);
        if (user != null && gameDate != null) {
            handleLogin(user.getUsername(), user.getPassword());
        }

        signupClickable.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            handleLogin(username, password);
        });

        loginViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (loginResponse != null) {
                if (loginResponse.getError().isEmpty()) {
                    preferencesHandler.saveUserData(this,
                            loginResponse.getGame().getUser(),
                            loginResponse.getGame().getDateString());
                    moveToHomeActivity(loginResponse);
                } else {
                    errorText.setText(loginResponse.getError());
                }
            }
        });

        loginViewModel.getErrorLiveData().observe(this, error -> {
            // Handle error
            if (error != null) {
                errorText.setText(error.toString());
            }
        });
    }

    private void handleLogin(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            loginViewModel.login(username, password);
        } else {
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToHomeActivity(LoginResponse loginResponse) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("GAME_DATA", loginResponse);
        startActivity(intent);
        finish();
    }
}