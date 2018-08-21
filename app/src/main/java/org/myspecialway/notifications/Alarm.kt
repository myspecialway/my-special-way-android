package org.myspecialway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.myspecialway.ui.agenda.ScheduleRenderModel

class Alarm(private val context: Context) {
    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    companion object {
        const val TITLE = "title"
    }

    private val alarmsQueue = mutableListOf<PendingIntent>()

    fun scheduleAlarm(scheduleModel: ScheduleRenderModel) {
        cancelAll()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(TITLE, scheduleModel.title)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmsQueue.add(pendingIntent)
        val triggerAtMillis = scheduleModel.time!!.date.time - System.currentTimeMillis()
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + triggerAtMillis, pendingIntent)
    }

    private fun cancelAll() = alarmsQueue.forEach { it.cancel() }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notificationIntent = Intent(context, NotificationActivity::class.java)
            notificationIntent.putExtra(NotificationActivity.MESSAGE_TEXT, "בוקר טוב זמן לשיעור ${intent.getStringExtra(TITLE)}")
            context.startActivity(notificationIntent)
        }
    }
}