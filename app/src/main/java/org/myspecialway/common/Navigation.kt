package org.myspecialway.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.login.LoginActivity
import org.myspecialway.ui.main.MainScreenActivity

object Navigation {

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toUnityNavigation(context: Context, schedule: ScheduleRenderModel?) {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", schedule?.title)
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }

    fun toMainActivity(activity: Activity) {
        val intent = Intent(activity, MainScreenActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun toLoginActivity(activity: Activity) {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun toNotificationActivity(context: Context, schedule: ScheduleRenderModel) {
        val intent = Intent(context, NotificationActivity::class.java)
        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, "בוקר טוב זמן לשיעור ${schedule.title}")
        intent.putExtra(NotificationActivity.SCHEDULE_KEY, schedule)
        context.startActivity(intent)
    }
}