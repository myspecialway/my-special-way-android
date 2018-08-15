package org.myspecialway.login.gateway;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserLoginRequest {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login")
    Call<LoginResponse> userLoginRequest(@Body JsonObject body);
}
