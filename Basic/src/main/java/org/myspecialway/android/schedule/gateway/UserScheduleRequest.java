package org.myspecialway.android.schedule.gateway;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserScheduleRequest {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    Call<ScheduleResponse> userScheduleRequest(@Body JsonObject body);
}
