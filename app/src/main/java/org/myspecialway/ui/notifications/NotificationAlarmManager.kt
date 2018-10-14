package org.myspecialway.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import org.myspecialway.common.Navigation
import org.myspecialway.common.addHour
import org.myspecialway.common.addMinutes
import org.myspecialway.ui.agenda.ScheduleRenderModel

class NotificationAlarmManager(private val context: Context) {
    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val alarmsQueue = mutableListOf<PendingIntent>()

    fun setAlarms(alarms: List<ScheduleRenderModel>) {
        alarmsQueue.cancelAll(alarmManager)


        alarms.forEach { alarm ->

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(NotificationActivity.NOTIFICATION_PAYLOAD, Gson().toJson(alarm.title))
            val id = System.currentTimeMillis().toInt()
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT)
            alarmsQueue.add(pendingIntent)
            val triggerAtMillis = alarm.time!!.date.time - System.currentTimeMillis()
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + triggerAtMillis,
                    pendingIntent
            )
        }

    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val payload = intent.getStringExtra(NotificationActivity.NOTIFICATION_PAYLOAD)
            val schedule = Gson().fromJson<ScheduleRenderModel>(payload, ScheduleRenderModel::class.java)

            val scheduleTime = schedule.time!!.date
            val startTime = scheduleTime.addMinutes(-10)
            val endTime = scheduleTime.addMinutes(10)

            if (scheduleTime.after(startTime) && scheduleTime.before(endTime)) {
                Navigation.toNotificationActivity(context, ScheduleRenderModel(title = schedule.title))
            }
        }
    }
}

fun MutableList<PendingIntent>.cancelAll(alarmManager: AlarmManager) {
    forEach { alarmManager.cancel(it) }
    clear()
}