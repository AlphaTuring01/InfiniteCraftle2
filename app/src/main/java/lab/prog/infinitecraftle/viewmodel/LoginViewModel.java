package lab.prog.infinitecraftle.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.service.AuthService;

public class LoginViewModel extends ViewModel {
    private final AuthService authService;
    private final MutableLiveData<LoginResponse> loginResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    public LoginViewModel() {
        this.authService = new AuthService();
        this.loginResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }

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