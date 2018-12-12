package org.myspecialway.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.inactive.InactiveScreenActivity
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.navigation.NavigationLocationsActivity
import org.myspecialway.ui.navigation.NavigationPasswordActivity
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.settings.SettingsActivity

object Navigation {

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toSettingsActivity(context: Context, locations : String) {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locations)

        context.startActivity(intent)
    }

    fun toUnityNavigation(context: Context, schedule :ScheduleRenderModel) {
        try {
            val intent = Intent()
            intent.component = ComponentName("org.myspecialway.navigation", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", schedule)
            context.startActivity(intent)
        } catch (e: Exception) { }
    }

    fun toUnityNavigation(context: Context, unityDest :String) {
        try {
            val intent = Intent()
            intent.component = ComponentName("org.myspecialway.navigation", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", unityDest)
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }

    fun toNavigationPassword(context: Context, locations: List<Location>) {
        val intent = Intent(context, NavigationPasswordActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, Gson().toJson(locations))

        context.startActivity(intent)
    }

    fun toMainActivity(activity: Activity) {
        val intent = Intent(activity, MainScreenActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun toInActivity(activity: Activity) {
        val intent = Intent(activity, InactiveScreenActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun toNotificationActivity(context: Context, current: ScheduleRenderModel, previous: ScheduleRenderModel) {
        val intent = Intent(context, NotificationActivity::class.java)
        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, "זמן לשיעור ${current.title}")
        intent.putExtra(NotificationActivity.SCHEDULE_CURRENT_KEY, current)
        intent.putExtra(NotificationActivity.SCHEDULE_PREVIOUS_KEY, previous)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toNavigationLocationsActivity(context: Context, locationsJson : String) {
        val intent = Intent(context, NavigationLocationsActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locationsJson)
        context.startActivity(intent)
    }
}