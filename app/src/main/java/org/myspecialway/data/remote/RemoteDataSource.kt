package org.myspecialway.data.remote

import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.login.LoginResponse
import org.myspecialway.ui.settings.SettingsResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteDataSource {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun fetchSchedule(@Body body: JsonObject): Single<ScheduleModel>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login")
    fun performLogin(@Body body: JsonObject): Single<LoginResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun fetchLocations(@Body body: JsonObject): Single<LocationModel>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun fetchSettings(@Body body: JsonObject): Single<SettingsResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("graphql")
    fun fetchBlockedSections(@Body body: JsonObject): Single<BlockedSectionsModel>
}