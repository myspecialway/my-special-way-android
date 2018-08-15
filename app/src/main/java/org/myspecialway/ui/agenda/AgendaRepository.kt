package org.myspecialway.ui.agenda

import android.location.Location
import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.dataSources.RemoteDataSource
import org.myspecialway.dataSources.local.LocalDataSource

interface AgendaRepository {
    fun getSchedule(): Single<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource
                           ) : AgendaRepository {
    override fun getSchedule(): Single<ScheduleModel> =
            remoteDataSource.userScheduleRequest(getPayLoad())

    private fun getPayLoad(): JsonObject {

        val jsonObject = JsonObject()
        jsonObject.addProperty("query", Constants.agendaQuery)
        jsonObject.addProperty("value", "")
        return jsonObject
    }
}