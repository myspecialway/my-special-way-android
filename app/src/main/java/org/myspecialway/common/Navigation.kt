package org.myspecialway.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.navigation.NavigationDestinationsActivity
import org.myspecialway.ui.navigation.NavigationPasswordActivity
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.settings.SettingsActivity

object Navigation {

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toSettingsActivity(context: Context) {
        context.startActivity(Intent(context, SettingsActivity::class.java))
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
        context.startActivity(Intent(context, NavigationPasswordActivity::class.java))
    }

    fun toNavigationDestinationsActivity(context: Context) {
        context.startActivity(Intent(context, NavigationDestinationsActivity::class.java))
    }

    fun toMainActivity(activity: Activity) {
        val intent = Intent(activity, MainScreenActivity::class.java)
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
}