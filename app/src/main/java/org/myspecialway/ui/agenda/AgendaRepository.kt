package org.myspecialway.ui.agenda

import android.util.Log
import android.util.Log.d
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.myspecialway.data.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource

interface AgendaRepository {
    fun getSchedule(): Observable<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource) : AgendaRepository {

    override fun getSchedule(): Observable<ScheduleModel> = getUsers()

    private fun getUsers(): Observable<ScheduleModel> =
            Observable.concatArray(
                getScheduleFromLocal(),
                getScheduleFromRemote())


    private fun getScheduleFromLocal(): Observable<ScheduleModel> =
            localDataSource.loadSchedule()
                .toObservable()
                .doOnNext {
                    d("AgendaRepositoryImpl", "Dispatching schedule from DB...")
                }

    private fun getScheduleFromRemote(): Observable<ScheduleModel>  =
            remoteDataSource.userScheduleRequest(getPayLoad()).toObservable()
                .doOnNext {
                    Log.d("AgendaRepositoryImpl", "Dispatching schedule from API...")
                    storeScheduleInDb(it)
                }


    private fun storeScheduleInDb(model: ScheduleModel) =
        Observable.fromCallable { localDataSource.saveAllSchedule(model) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { d("AgendaRepositoryImpl", "Inserted Schedule from API in DB...") }


    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", Constants.AGENDA_QUERY)
        json.addProperty("value", "")
        return json
    }
}