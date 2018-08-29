package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Flowable
import org.myspecialway.common.filterAtError
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource
import org.myspecialway.session.SessionManager

interface AgendaRepository {
    fun getSchedule(): Flowable<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource,
                           private val sessionManager: SessionManager) : AgendaRepository {

    override fun getSchedule(): Flowable<ScheduleModel> =
            Flowable.concatArrayEager(local(),remote()).firstOrError().toFlowable()

    private fun remote() = remoteDataSource.fetchSchedule(getPayLoad())
            .toFlowable()
            .filterAtError()
            .doOnNext { localDataSource.saveAllSchedule(it) }

    private fun local() = localDataSource.loadSchedule()
            .toFlowable()
            .filterAtError()

    /**
     * Build Json object with the payload needed to query the backend
     */
    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", query(sessionManager.getUserModel().id ?: ""))
        json.addProperty("value", "")
        return json
    }
}
