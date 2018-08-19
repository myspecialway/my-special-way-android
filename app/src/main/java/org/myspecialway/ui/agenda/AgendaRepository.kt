package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Single
import org.myspecialway.data.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource

interface AgendaRepository {
    fun getSchedule(): Single<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource) : AgendaRepository {

    override fun getSchedule(): Single<ScheduleModel> =
            remoteDataSource.userScheduleRequest(getPayLoad())
                    .doAfterSuccess { localDataSource.saveAllSchedule(it) }

    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", Constants.AGENDA_QUERY)
        json.addProperty("value", "")
        return json
    }
}