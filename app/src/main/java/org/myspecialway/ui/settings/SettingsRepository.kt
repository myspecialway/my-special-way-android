package org.myspecialway.ui.settings

import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.ui.agenda.settingsQuery


interface SettingsRepository {
    fun fetchSettings()
    fun getTeacherCode(): Int
}

class SettingsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : SettingsRepository {

    private var teacherCode: Int = -1

    override fun getTeacherCode(): Int {
        return teacherCode
    }

    override fun fetchSettings() {

        remoteDataSource.fetchSettings(getSettingsPayLoad())
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {
                            teacherCode = it.data.settings[0].teachercode
                        }
                        ,
                        {
                            //TODO error handling it.message
                        })


    }

    private fun getSettingsPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", settingsQuery())
        return json
    }

}