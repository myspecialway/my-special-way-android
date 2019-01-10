package org.myspecialway.ui.shared

import android.content.SharedPreferences
import com.google.gson.JsonObject
import io.reactivex.Flowable
import org.myspecialway.common.filterAtError
import org.myspecialway.data.local.LocalDataSource
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.login.UserModel

interface AgendaRepository {
    fun getSchedule(): Flowable<ScheduleModel>
    fun getLocations(): Flowable<LocationModel>
    fun getBlockedSections(): Flowable<BlockedSectionsModel>
    fun saveLocations(locations: List<Location>)
}

class AgendaRepositoryImpl(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource,
                           private val sp: SharedPreferences) : AgendaRepository {
    override fun getSchedule(): Flowable<ScheduleModel> =
            Flowable.concatArrayEager(local(), remote())
                    .firstOrError()
                    .toFlowable()

    override fun saveLocations(locations: List<Location>) = localDataSource.saveLocations(locations)

    private fun remote() = remoteDataSource.fetchSchedule(getSchedulePayLoad())
            .toFlowable()
            .filterAtError()
            .doOnNext { localDataSource.saveAllSchedule(it) }

    private fun local() = localDataSource.loadSchedule()
            .toFlowable()
            .filterAtError()

    /**
     * Build Json object with the payload needed to query the backend
     */
    private fun getSchedulePayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", query(UserModel().getUser(sp).id ?: ""))
        json.addProperty("value", "")
        return json
    }

    override fun getLocations(): Flowable<LocationModel> = Flowable.concatArrayEager(localLocations(), remoteLocations())
            .firstOrError()
            .toFlowable()

    override fun getBlockedSections(): Flowable<BlockedSectionsModel> = Flowable.concatArrayEager(localBlockedSections(), remoteBlockedSections())
            .firstOrError()
            .toFlowable()


    private fun remoteLocations() =
            remoteDataSource
            .fetchLocations(getLocationsPayLoad())
            .toFlowable()
            .filterAtError()
            .doOnNext { localDataSource.saveLocations(it) }

    private fun remoteBlockedSections() =
            remoteDataSource
                    .fetchBlockedSections(getBlockedSectionsPayLoad())
                    .toFlowable()
                    .filterAtError()
                    .doOnNext { localDataSource.saveBlockedSections(it) }

    private fun localLocations() = localDataSource.loadLocations()
            .toFlowable()
            .filterAtError()

    private fun localBlockedSections() = localDataSource.loadBlockedSections()
            .toFlowable()
            .filterAtError()

    private fun getLocationsPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", locationQuery())
        json.addProperty("value", "")
        return json
    }

    private fun getBlockedSectionsPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", blockedSectionsQuery())
        json.addProperty("value", "")
        return json
    }
}