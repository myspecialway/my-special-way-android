package org.myspecialway.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.preference.PreferenceManager
import org.myspecialway.R
import com.google.gson.Gson
import org.myspecialway.di.RemoteProperties
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.inactive.InactiveScreenActivity
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.navigation.NavigationLocationsActivity
import org.myspecialway.ui.navigation.NavigationPasswordActivity
import org.myspecialway.ui.notifications.MedicineReminderActivity
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.settings.SettingsActivity

object Navigation {
    var navLocations: List<Location> = listOf()
    var naBlockedEdges: List<BlockedSection> = listOf()

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toSettingsActivity(context: Context, locations: String) {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locations)

        context.startActivity(intent)
    }

    fun toUnityNavigation(context: Context, dest: String, imageName : String?, destination: DestinationType) {

        var destinationType = ""
        var imageUrl : String? = null

        when(destination){
            DestinationType.REGULAR->{
                destinationType = "regular"

                if(!imageName.isNullOrEmpty()){
                    val iconName = imageName?.replace(".png","")
                    imageUrl = "${RemoteProperties.BASE_URL_IMAGES}${iconName}.png"
                }
            }

            DestinationType.TOILET->{
                destinationType = "special"
            }

            DestinationType.MEDICINE->{
                destinationType = "special"
            }
        }

        try {
            val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val intent = Intent()
            intent.component = ComponentName("org.myspecialway.navigation", "com.unity3d.player.UnityPlayerActivity")

            val locations = Gson().toJson(navLocations)
            val blocked = Gson().toJson(naBlockedEdges)
            val sound = defaultSharedPreferences.getBoolean(context.getString(R.string.settings_sound_key), false)
            val gender = UserModel().getUser(defaultSharedPreferences).gender

            intent.putExtra("type", destinationType)
            intent.putExtra("destination", dest)
            intent.putExtra("allNodes", locations)
            intent.putExtra("blockedEdges", blocked)
            intent.putExtra("sound", sound)
            intent.putExtra("gender", gender?.toUpperCase())
            intent.putExtra("iconUrl", imageUrl)

            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }

    fun toNavigationPassword(context: Context) {
        val intent = Intent(context, NavigationPasswordActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, Gson().toJson(navLocations))
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

    fun toNotificationActivity(context: Context, current: ScheduleRenderModel?, previous: ScheduleRenderModel?, reminderType: ReminderType, soundNotifications: Boolean) {
        val intent = Intent(context, NotificationActivity::class.java)
        val scheduleTitle = when (reminderType) {
            ReminderType.MEDICINE -> context.getString(R.string.time_for_medicine)
            ReminderType.REHAB -> context.getString(R.string.time_for_rehab)
            ReminderType.SCHEDULE -> context.getString(R.string.time_for_lesson, current?.title)
        }

        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, scheduleTitle)
        intent.putExtra(NotificationActivity.SCHEDULE_CURRENT_KEY, current)
        intent.putExtra(NotificationActivity.SCHEDULE_PREVIOUS_KEY, previous)
        intent.putExtra(NotificationActivity.REMINDER_TYPE_KEY, reminderType.name)
        intent.putExtra(NotificationActivity.SOUND_NOTIFICATIONS, soundNotifications)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toMedicineReminderActivity(context: Context, soundNotifications: Boolean) {
        val intent = Intent(context, MedicineReminderActivity::class.java)
        val scheduleTitle = context.getString(R.string.time_for_medicine)

        intent.putExtra(MedicineReminderActivity.NOTIFICATION_TITLE, scheduleTitle)
        intent.putExtra(MedicineReminderActivity.REMINDER_TYPE_KEY, ReminderType.MEDICINE.name)
        intent.putExtra(MedicineReminderActivity.SOUND_NOTIFICATIONS, soundNotifications)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toNavigationLocationsActivity(context: Context, locationsJson: String) {
        val intent = Intent(context, NavigationLocationsActivity::class.java)
        intent.putExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locationsJson)
        context.startActivity(intent)
    }
}