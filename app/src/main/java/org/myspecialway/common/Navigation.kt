package org.myspecialway.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.myspecialway.R
import com.google.gson.Gson
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.ReminderType
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.inactive.InactiveScreenActivity
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.navigation.NavigationLocationsActivity
import org.myspecialway.ui.navigation.NavigationPasswordActivity
import org.myspecialway.ui.notifications.MedicineReminderActivity
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.settings.SettingsActivity

object Navigation {
    var navLocations: List<Location> = listOf()

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

    fun toNavigationPassword(context: Context) {
        val intent = Intent(context, NavigationPasswordActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, Gson().toJson(navLocations))
        context.startActivity(intent)


    }

    fun toNavigationPassword(context: Context, unityDest: String ) {
        val intent = Intent(context, NavigationPasswordActivity::class.java)
        intent.putExtra(NavigationPasswordActivity.UNITY_DEST_KEY, unityDest)
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

    fun toNotificationActivity(context: Context, current: ScheduleRenderModel?, previous: ScheduleRenderModel?, reminderType: ReminderType) {
        val intent = Intent(context, NotificationActivity::class.java)
        val scheduleTitle  = when (reminderType){
            ReminderType.MEDICINE -> context.getString(R.string.time_for_medicine)
            ReminderType.REHAB -> context.getString(R.string.time_for_rehab)
            ReminderType.SCHEDULE -> context.getString(R.string.time_for_lesson ,current?.title)
        }

        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, scheduleTitle)
        intent.putExtra(NotificationActivity.SCHEDULE_CURRENT_KEY, current)
        intent.putExtra(NotificationActivity.SCHEDULE_PREVIOUS_KEY, previous)
        intent.putExtra(NotificationActivity.REMINDER_TYPE_KEY, reminderType.name)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toMedicineReminderActivity(context: Context) {
        val intent = Intent(context, MedicineReminderActivity::class.java)
        val scheduleTitle  = context.getString(R.string.time_for_medicine)

        intent.putExtra(MedicineReminderActivity.NOTIFICATION_TITLE, scheduleTitle)
        intent.putExtra(MedicineReminderActivity.REMINDER_TYPE_KEY, ReminderType.MEDICINE.name)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toNavigationLocationsActivity(context: Context, locationsJson : String) {
        val intent = Intent(context, NavigationLocationsActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locationsJson)
        context.startActivity(intent)
    }
}