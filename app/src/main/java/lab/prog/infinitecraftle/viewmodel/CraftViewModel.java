package lab.prog.infinitecraftle.viewmodel;

import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.CraftResponse;
import lab.prog.infinitecraftle.service.GameService;

public class CraftViewModel extends ViewModel {

    private final GameService gameService;

    public CraftViewModel() {
        gameService = new GameService();
    }

    public void craftElement(CraftRequest craftRequest, CraftResponseCallback<CraftResponse> callback) {
        gameService.craftElement(craftRequest, new GameService.ResponseCallback<CraftResponse>() {
            @Override
            public void onResponse(CraftResponse response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public interface CraftResponseCallback<T> {
        void onResponse(T response);
        void onFailure(Exception e);
    }
}