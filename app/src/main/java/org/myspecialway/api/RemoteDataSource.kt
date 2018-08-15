package org.myspecialway.api

import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.schedule.gateway.ScheduleResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteDataSource {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun userScheduleRequest(@Body body: JsonObject): Single<ScheduleResponse>
}