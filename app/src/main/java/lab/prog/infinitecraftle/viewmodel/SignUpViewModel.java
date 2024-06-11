package lab.prog.infinitecraftle.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.service.AuthService;
import lab.prog.infinitecraftle.dto.SignUpResponse;

public class SignUpViewModel extends ViewModel {
    private final AuthService authService;
    private final MutableLiveData<SignUpResponse> SignUpResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    public SignUpViewModel() {
        this.authService = new AuthService();
        this.SignUpResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<SignUpResponse> getSignUpResponseLiveData() {
        return SignUpResponseLiveData;
    }

    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }

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