package lab.prog.infinitecraftle.viewmodel;

import androidx.lifecycle.ViewModel;

import lab.prog.infinitecraftle.dto.CraftResponse;
import lab.prog.infinitecraftle.service.GameService;

public class CraftViewModel extends ViewModel {

    private final GameService gameService;

    public CraftViewModel() {
        gameService = new GameService();
    }

    public void craftElement(int userId, String gameDate, String parent1, String parent2, CraftResponseCallback<CraftResponse> callback) {
        gameService.craftElement(userId, gameDate, parent1, parent2, new GameService.ResponseCallback<CraftResponse>() {
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