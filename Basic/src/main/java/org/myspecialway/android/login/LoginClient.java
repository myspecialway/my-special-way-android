package org.myspecialway.android.login;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginClient {

    private LoginAccessToken loginAccessToken ;

    public void login(String username, String password){
        //Adding logs
        HttpLoggingInterceptor interceptor = getInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //Creating retrofit client
        Retrofit.Builder builder = getRetrofitBuilder(client);
        Retrofit retrofit = builder.build();

        //Creating user login infra
        IUserLogin userLogin = retrofit.create(IUserLogin.class);

        //Populating parameters to json
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        object.addProperty("password", password);

        //Creating retrofit call
        Call<LoginAccessToken> call = userLogin.userLoginRequest(object,"application/json");

        //Placing call in execution queue
        call.enqueue(new Callback<LoginAccessToken>() {

            //Implementing callback
            @Override
            public void onResponse(Call<LoginAccessToken> call, Response<LoginAccessToken> response) {
                System.out.println("onResponse");
                if (response.isSuccessful()) {
                    loginAccessToken = response.body();
                } else {
                    //TODO - implement request failure
                }
            }

            @Override
            public void onFailure(Call<LoginAccessToken> call, Throwable t) {
                //TODO - implement on failure of sending the request
            }
        });
    }

    @NonNull
    private Retrofit.Builder getRetrofitBuilder(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(LoginConstants.MSW_SERVER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());
    }

    @NonNull
    private HttpLoggingInterceptor getInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        });
    }


    public LoginAccessToken getLoginAccessToken() {
        return loginAccessToken;
    }
}
