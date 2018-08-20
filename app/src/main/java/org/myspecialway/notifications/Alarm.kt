package org.myspecialway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.myspecialway.ui.agenda.ScheduleRenderModel

class Alarm(private val context: Context) {
    private var am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var pendingIntent: PendingIntent? = null

    companion object {
        const val TITLE = "title"
    }

    fun scheduleAlarm(scheduleModel: ScheduleRenderModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(TITLE, scheduleModel.title)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerAtMillis = scheduleModel.time!!.date.time - System.currentTimeMillis()
        am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notificationIntent = Intent(context, NotificationActivity::class.java)
            notificationIntent.putExtra(NotificationActivity.MESSAGE_TEXT, "בוקר טוב זמן לשיעור ${intent.getStringExtra(TITLE)}")
            context.startActivity(notificationIntent)
        }
    }
}