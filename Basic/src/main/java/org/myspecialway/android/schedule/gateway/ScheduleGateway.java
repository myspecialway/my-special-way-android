package org.myspecialway.android.schedule.gateway;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import org.myspecialway.android.login.RequestCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScheduleGateway implements IScheduleGateway {

    private static final int HTTP_STATUS_CODE_BAD_REQUEST = 400;

    private final Retrofit retrofit;

    public ScheduleGateway(Retrofit retrofit){
        this.retrofit = retrofit;
    }

    @Override
    public void getUserSchedule(@NonNull String scheduleQuery, @NonNull String userAccessToken, @NonNull final RequestCallback<List<ScheduleResponse.Schedule>> callback){
        //Creating user login infra
        UserScheduleRequest userSchedule = retrofit.create(UserScheduleRequest.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("query", scheduleQuery);
        jsonObject.addProperty("value", "");

        //Creating retrofit call
        Call<ScheduleResponse> call = userSchedule.userScheduleRequest(jsonObject);

        //Placing call in execution queue
        call.enqueue(new Callback<ScheduleResponse>() {

            //Implementing callback
            @Override
            public void onResponse(@NonNull Call<ScheduleResponse> call, @NonNull Response<ScheduleResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData().getClassById().getSchedule());
                } else if(response.code() == HTTP_STATUS_CODE_BAD_REQUEST){

                    callback.onFailure(new BadScheduleRequest());
                }
                else{
                    callback.onFailure(new HttpException(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });

    }
}
