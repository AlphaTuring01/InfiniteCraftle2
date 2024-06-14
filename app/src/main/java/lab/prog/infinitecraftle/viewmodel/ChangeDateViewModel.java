package lab.prog.infinitecraftle.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.ChangeDateResponse;
import lab.prog.infinitecraftle.dto.ChangeDateResponse;
import lab.prog.infinitecraftle.service.GameService;

/**
 * ViewModel for changing the date of the game.
 */
public class ChangeDateViewModel extends ViewModel {

    private final GameService gameService;
    private final MutableLiveData<ChangeDateResponse> changeDateResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    /**
     * Constructor for ChangeDateViewModel.
     */
    public ChangeDateViewModel() {
        gameService = new GameService();
        this.changeDateResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    /**
     * Getter for the error live data.
     * @return The error live data.
     */
    public LiveData<Exception> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Getter for the change date response live data.
     * @return The change date response live data.
     */
    public LiveData<ChangeDateResponse> getChangeDateResponseLiveData() {
        return changeDateResponseLiveData;
    }

    /**
     * Method to change the date of the game.
     * @param userId The user id.
     * @param date The date to change to.
     */
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