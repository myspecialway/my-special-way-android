package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.myspecialway.common.handleError
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource
import java.util.concurrent.TimeUnit

interface AgendaRepository {
    fun getSchedule(): Observable<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource) : AgendaRepository {

    override fun getSchedule(): Observable<ScheduleModel> =
            Observable.concatArrayEager(
                    localDataSource.loadSchedule().toObservable().handleError(),
                    remoteDataSource.fetchSchedule(getPayLoad()).toObservable().handleError()
                            .doOnNext { localDataSource.saveAllSchedule(it) })

    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", query())
        json.addProperty("value", "")
        return json
    }
}
