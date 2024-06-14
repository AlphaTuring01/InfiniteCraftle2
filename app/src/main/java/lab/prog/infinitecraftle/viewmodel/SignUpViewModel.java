package lab.prog.infinitecraftle.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.service.AuthService;
import lab.prog.infinitecraftle.dto.SignUpResponse;


/**
 * ViewModel for signing up.
 */
public class SignUpViewModel extends ViewModel {
    private final AuthService authService;
    private final MutableLiveData<SignUpResponse> SignUpResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    /**
     * Constructor for SignUpViewModel.
     */
    public SignUpViewModel() {
        this.authService = new AuthService();
        this.SignUpResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    /**
     * Getter for the sign up response live data.
     * @return The sign up response live data.
     */
    public LiveData<SignUpResponse> getSignUpResponseLiveData() {
        return SignUpResponseLiveData;
    }

    /**
     * Getter for the error live data.
     * @return The error live data.
     */
    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Method to sign up.
     * @param username The username.
     * @param password The password.
     * @param confirmPassword The password confirmation.
     */
    public void signup(String username, String password, String confirmPassword) {
        authService.signup(username, password, confirmPassword, new AuthService.ResponseCallback<SignUpResponse>() {
            @Override
            public void onResponse(SignUpResponse response) {
                SignUpResponseLiveData.postValue(response);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e);
            }
        });
    }
}