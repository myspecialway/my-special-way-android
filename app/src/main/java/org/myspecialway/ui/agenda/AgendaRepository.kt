package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.myspecialway.common.filterAtError
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource

interface AgendaRepository {
    fun getSchedule(): Observable<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource) : AgendaRepository {

    override fun getSchedule(): Observable<ScheduleModel> =
            Observable.concatArrayEager(2,2, local(),remote())

    private fun remote() = remoteDataSource.fetchSchedule(getPayLoad())
            .toObservable()
            .filterAtError()
            .doOnNext { localDataSource.saveAllSchedule(it) }

    private fun local() = localDataSource.loadSchedule()
            .toObservable()


    /**
     * Build Json object with the payload needed to query the backend
     */
    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", query())
        json.addProperty("value", "")
        return json
    }
}
