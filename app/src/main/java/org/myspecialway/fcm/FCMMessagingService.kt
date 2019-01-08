package org.myspecialway.fcm

import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.myspecialway.data.local.LocalDataSource
import org.myspecialway.data.remote.RemoteDataSource
import org.myspecialway.ui.agenda.locationQuery
import org.myspecialway.ui.agenda.query
import org.myspecialway.ui.login.UserModel


class FCMMessagingService : FirebaseMessagingService() {
    private val TAG = this@FCMMessagingService.javaClass.simpleName

    private val remote by inject<RemoteDataSource>()
    private val local by inject<LocalDataSource>()

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: " + token!!)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.apply()
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a notification payload.
        if (remoteMessage.data != null) {
            updateLocations()
            updateSchedule()
        }
    }

    private fun updateSchedule() {
        remote.fetchSchedule(getSchedulePayLoad())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            local.deleteAllSchedule()
                            local.saveAllSchedule(it)
                        },
                        { })
    }

    private fun updateLocations() {
        remote.fetchLocations(getLocationsPayLoad())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            local.deleteAllLocations()
                            local.saveLocations(it)
                        },
                        { })
    }

    private fun getLocationsPayLoad(): JsonObject {
        val json = JsonObject()
        json.addProperty("query", locationQuery())
        json.addProperty("value", "")
        return json
    }

    private fun getSchedulePayLoad(): JsonObject {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val json = JsonObject()
        json.addProperty("query", query(UserModel().getUser(sharedPref).id ?: ""))
        json.addProperty("value", "")
        return json
    }
}
