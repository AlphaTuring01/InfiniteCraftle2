package lab.prog.infinitecraftle.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.ChangeDateResponse;
import lab.prog.infinitecraftle.dto.ChangeDateResponse;
import lab.prog.infinitecraftle.service.GameService;

public class ChangeDateViewModel extends ViewModel {

    private final GameService gameService;
    private final MutableLiveData<ChangeDateResponse> changeDateResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    public ChangeDateViewModel() {
        gameService = new GameService();
        this.changeDateResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }
    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }
    public LiveData<ChangeDateResponse> getChangeDateResponseLiveData() {
        return changeDateResponseLiveData;
    }
    public void changeDate(int userId, String date) {
        gameService.changeDate(userId, date, new GameService.ResponseCallback<ChangeDateResponse>() {
            @Override
            public void onResponse(ChangeDateResponse response) {
                changeDateResponseLiveData.postValue(response);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e);
            }
        });
    }
}