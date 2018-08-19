package org.myspecialway.ui.agenda

import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.myspecialway.data.RemoteDataSource
import org.myspecialway.data.local.LocalDataSource

interface AgendaRepository {
    fun getSchedule(): Observable<ScheduleModel>
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource) : AgendaRepository {

    override fun getSchedule(): Observable<ScheduleModel> =
            remoteDataSource.userScheduleRequest(getPayLoad())
                    .toObservable()
                    .doOnNext { localDataSource.saveAllSchedule(it) }
                    .publish { remote ->
                        Observable.merge(
                                remote,
                                localDataSource
                                        .loadSchedule()
                                        .toObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .takeUntil(remote))
                    }


    private fun getPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", Constants.AGENDA_QUERY)
        json.addProperty("value", "")
        return json
    }
}