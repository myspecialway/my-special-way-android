package org.myspecialway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel

class Alarm(private val context: Context) {
    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    companion object {
        const val TITLE = "title"
    }

    private val alarmsQueue = mutableListOf<PendingIntent>()

    fun scheduleAlarm(scheduleModel: ScheduleRenderModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(NotificationActivity.SCHEDULE_KEY, scheduleModel)
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmsQueue.add(pendingIntent)
        val triggerAtMillis = scheduleModel.time!!.date.time - System.currentTimeMillis()
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + triggerAtMillis,
                pendingIntent
        )
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val schedule = intent.getParcelableExtra<ScheduleRenderModel>(NotificationActivity.SCHEDULE_KEY)
            Navigation.toNotificationActivity(context, schedule)
        }
    }
}

fun MutableList<PendingIntent>.cancelAll() = forEach { it.cancel() }