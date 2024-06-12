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

        String userId = preferencesHandler.getUserId(this);
        String gameDate = preferencesHandler.getGameDate(this);
        if (userId != null && gameDate != null) {
            moveToHomeActivity();
            return;
        }

        signupClickable.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                // Call login method of LoginViewModel
                loginViewModel.login(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (loginResponse != null) {
                if (loginResponse.getError().isEmpty()) {
                    preferencesHandler.saveUserData(this,
                            String.valueOf(loginResponse.getGame().getUser().getId()),
                            loginResponse.getGame().getDateString());
                    moveToHomeActivity();
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

    private void moveToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}