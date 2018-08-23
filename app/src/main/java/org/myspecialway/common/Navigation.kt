package org.myspecialway.common

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.myspecialway.notifications.Alarm
import org.myspecialway.notifications.NotificationActivity
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.ScheduleRenderModel

object Navigation {

    fun toScheduleActivity(context: Context) {
        context.startActivity(Intent(context, AgendaActivity::class.java))
    }

    fun toUnityNavigation(context: Context, schedule: ScheduleRenderModel) {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity")
            intent.putExtra("destination", schedule.title)
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }

    fun toNotificationActivity(context: Context, schedule: ScheduleRenderModel) {
        val intent = Intent(context, NotificationActivity::class.java)
        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, "בוקר טוב זמן לשיעור ${intent.getStringExtra(Alarm.TITLE)}")
        intent.putExtra(NotificationActivity.SCHEDULE_KEY, schedule)
        context.startActivity(intent)
    }
}