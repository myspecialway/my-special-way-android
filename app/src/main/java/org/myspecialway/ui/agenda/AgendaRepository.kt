package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.api.RemoteDataSource
import org.myspecialway.schedule.gateway.ScheduleResponse

interface AgendaRepository {
    fun getSchedule(): Single<ScheduleResponse>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource) : AgendaRepository {
    override fun getSchedule(): Single<ScheduleResponse> =
            remoteDataSource.userScheduleRequest(getPayLoad())

    private fun getPayLoad(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("query", Constants.agendaQuery)
        jsonObject.addProperty("value", "")
        return jsonObject
    }
}