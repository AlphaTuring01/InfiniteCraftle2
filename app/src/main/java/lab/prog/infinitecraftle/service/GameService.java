package lab.prog.infinitecraftle.service;

import com.google.gson.Gson;

import java.io.IOException;

import lab.prog.infinitecraftle.dto.ChangeDateResponse;
import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.CraftResponse;
import lab.prog.infinitecraftle.dto.LoginResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * GameService class is used to send the game requests to the server.
 * It uses the OkHttpClient to send the requests and the Gson library to parse the responses.
 */
public class GameService {

    private static final String BASE_URL = "http://172.15.1.60:8000/api/";
    private final OkHttpClient client;
    private final Gson gson;

    /**
     * Constructor for GameService.
     */
    public GameService() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    /**
     * Method to send a craft request to the server.
     * @param craftRequest The craft request.
     * @param callback The callback to be called when the response is received.
     */
    public void craftElement(CraftRequest craftRequest, ResponseCallback<CraftResponse> callback) {
        String urlString = BASE_URL + "craft";
        String requestBody = "{\"userId\":" + craftRequest.getUserId() + ",\"gameDate\":\"" +
                craftRequest.getGameDateString() + "\", \"parent1\":\"" + craftRequest.getParent1() +
                "\",\"parent2\":\"" + craftRequest.getParent2() + "\"}";

        Request request = new Request.Builder()
                .url(urlString)
                .post(RequestBody.create(requestBody.getBytes()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    CraftResponse craftResponse = gson.fromJson(response.body().string(), CraftResponse.class);
                    callback.onResponse(craftResponse);
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    /**
     * Method to send a change date request to the server.
     * @param userId The user id.
     * @param date The date.
     * @param callback The callback to be called when the response is received.
     */
    public void changeDate(int userId, String date, ResponseCallback<ChangeDateResponse> callback) {
        String urlString = BASE_URL + "change-date?userId=" + userId + "&" + "date=" + date;
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ChangeDateResponse changeResponse = gson.fromJson(response.body().string(), ChangeDateResponse.class);
                    callback.onResponse(changeResponse);
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    /**
     * Interface to handle the response from the server.
     * @param <T> The type of the response.
     */
    public interface ResponseCallback<T> {
        void onResponse(T response);
        void onFailure(Exception e);
    }
}
