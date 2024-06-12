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
        moveToHomeActivity();
        // Initialize ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Find views
        EditText usernameEditText = findViewById(R.id.loginEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView errorText = findViewById(R.id.errorText);
        // Set OnClickListener for loginButton
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

        // Observe LiveData from LoginViewModel
        loginViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            // Handle login response
            if (loginResponse != null) {
                if (loginResponse.getError().isEmpty()) {
                    // Move to home activity if login is successful
                    moveToHomeActivity();
                } else {
                    errorText.setText(loginResponse.getError());
                    // Display error message if login fails
                    //Toast.makeText(LoginActivity.this, loginResponse.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginViewModel.getErrorLiveData().observe(this, error -> {
            // Handle error
            if (error != null) {
                errorText.setText(error.toString());
                //Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Finish current activity to prevent going back to LoginActivity
    }
}