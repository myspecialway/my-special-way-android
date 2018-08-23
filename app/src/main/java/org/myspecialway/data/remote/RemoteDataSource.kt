package org.myspecialway.data.remote

import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.ui.agenda.ScheduleModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteDataSource {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun fetchSchedule(@Body body: JsonObject): Single<ScheduleModel>
}