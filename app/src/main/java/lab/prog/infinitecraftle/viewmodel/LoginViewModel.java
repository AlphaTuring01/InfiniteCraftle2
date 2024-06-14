package lab.prog.infinitecraftle.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.service.AuthService;

/**
 * ViewModel for logging in.
 */
public class LoginViewModel extends ViewModel {
    private final AuthService authService;
    private final MutableLiveData<LoginResponse> loginResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    /**
     * Constructor for LoginViewModel.
     */
    public LoginViewModel() {
        this.authService = new AuthService();
        this.loginResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    /**
     * Getter for the login response live data.
     * @return The login response live data.
     */
    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    /**
     * Getter for the error live data.
     * @return The error live data.
     */
    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Method to log in.
     * @param username The username.
     * @param password The password.
     */
    public void login(String username, String password) {
        authService.login(username, password, new AuthService.ResponseCallback<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                loginResponseLiveData.postValue(response);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e);
            }
        });
    }
}