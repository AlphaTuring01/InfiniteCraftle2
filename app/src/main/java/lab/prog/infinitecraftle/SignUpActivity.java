package lab.prog.infinitecraftle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import lab.prog.infinitecraftle.viewmodel.SignUpViewModel;


/**
 * Activity for signing up.
 */
public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel signUpViewModel;

    /**
     * Method to create the activity.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        EditText usernameEditText = findViewById(R.id.loginEditText); // Assuming the ID is the same as in the login layout
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button signupButton = findViewById(R.id.signupButton);
        TextView errorText = findViewById(R.id.errorText);

        signupButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                // Call signup method of SignUpViewModel
                signUpViewModel.signup(username, password, confirmPassword);
            } else {
                Toast.makeText(SignUpActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        signUpViewModel.getSignUpResponseLiveData().observe(this, signUpResponse -> {
            if (signUpResponse != null) {
                if (signUpResponse.getError().isEmpty()) {
                    moveToLoginActivity();
                } else {
                    errorText.setText(signUpResponse.getError());
                }
            }
        });

        signUpViewModel.getErrorLiveData().observe(this, error -> {
            // Handle error
            if (error != null) {
                errorText.setText(error.toString());
            }
        });
    }

    /**
     * Method to move to the login activity.
     */
    private void moveToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}