package lab.prog.infinitecraftle.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.CraftResponse;
import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.service.GameService;

/**
 * ViewModel for crafting an element.
 */
public class CraftViewModel extends ViewModel {

    private final GameService gameService;
    private final MutableLiveData<CraftResponse> craftResponseLiveData;
    private final MutableLiveData<Exception> errorLiveData;

    /**
     * Constructor for CraftViewModel.
     */
    public CraftViewModel() {
        gameService = new GameService();
        this.craftResponseLiveData = new MutableLiveData<>();
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
     * Getter for the craft response live data.
     * @return The craft response live data.
     */
    public LiveData<CraftResponse> getCraftResponseLiveData() {
        return craftResponseLiveData;
    }

    /**
     * Method to craft an element.
     * @param craftRequest The craft request.
     */
    public void craftElement(CraftRequest craftRequest) {
        gameService.craftElement(craftRequest, new GameService.ResponseCallback<CraftResponse>() {
            @Override
            public void onResponse(CraftResponse response) {
                craftResponseLiveData.postValue(response);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e);
            }
        });
    }
}