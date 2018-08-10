package org.myspecialway.android.login.gateway;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.myspecialway.android.login.RequestCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginGateway  implements ILoginGateway {

    private static final int HTTP_STATUS_CODE_UNAUTHORIZED = 401;

    private final Retrofit retrofit;

    public LoginGateway(Retrofit retrofit){
        this.retrofit = retrofit;
    }

    @Override
    public void login(@NonNull String username, @NonNull String password, @NonNull final RequestCallback<String> callback){
        //Creating user login infra
        UserLoginRequest userLogin = retrofit.create(UserLoginRequest.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        //Creating retrofit call
        Call<LoginResponse> call = userLogin.userLoginRequest(jsonObject);

        //Placing call in execution queue
        call.enqueue(new Callback<LoginResponse>() {

            //Implementing callback
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().accessToken);
                } else if(response.code() == HTTP_STATUS_CODE_UNAUTHORIZED){

                    callback.onFailure(new InvalidLoginCredentials());
                }
                else{
                    callback.onFailure(new HttpException(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });

    }
}
