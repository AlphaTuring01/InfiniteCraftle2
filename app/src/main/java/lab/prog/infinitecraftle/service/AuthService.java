package lab.prog.infinitecraftle.service;
import com.google.gson.Gson;

import java.io.IOException;

import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.dto.SignUpResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthService class is used to send the login and signup requests to the server.
 * It uses the OkHttpClient to send the requests and the Gson library to parse the responses.
 */
public class AuthService {

    private static final String BASE_URL = "http://172.15.1.60:8000/api/";
    private final OkHttpClient client;
    private final Gson gson;

    /**
     * Constructor for AuthService.
     */
    public AuthService() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    /**
     * Method to send a login request to the server.
     * @param username The username.
     * @param password The password.
     * @param callback The callback to be called when the response is received.
     */
    public void login(String username, String password, ResponseCallback<LoginResponse> callback) {
        String urlString = BASE_URL + "login?username=" + username + "&password=" + password;
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
                    LoginResponse loginResponse = gson.fromJson(response.body().string(), LoginResponse.class);
                    callback.onResponse(loginResponse);
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    /**
     * Method to send a signup request to the server.
     * @param username The username.
     * @param password The password.
     * @param confirmPassword The confirmation password.
     * @param callback The callback to be called when the response is received.
     */
    public void signup(String username, String password, String confirmPassword, ResponseCallback<SignUpResponse> callback) {
        String urlString = BASE_URL + "signup?username=" + username + "&password=" + password + "&confirm-password=" + confirmPassword;
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
                    SignUpResponse SignUpResponse = gson.fromJson(response.body().string(), SignUpResponse.class);
                    callback.onResponse(SignUpResponse);
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    /**
     * Interface used to define the callback methods for the response.
     * @param <T> The type of the response.
     */
    public interface ResponseCallback<T> {
        void onResponse(T response);
        void onFailure(Exception e);
    }
}