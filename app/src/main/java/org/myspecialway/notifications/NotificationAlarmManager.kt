package org.myspecialway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel
import java.util.*

class NotificationAlarmManager(private val context: Context) {
    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val alarmsQueue = mutableListOf<PendingIntent>()

    fun scheduleAlarm(scheduleModel: ScheduleRenderModel) {
        alarmsQueue.cancelAll()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, scheduleModel.title)
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmsQueue.add(pendingIntent)
        val triggerAtMillis = scheduleModel.time!!.date.time - System.currentTimeMillis()

        val date = Date()
        date.time = System.currentTimeMillis() + triggerAtMillis
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,
                pendingIntent
        )
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val scheduleTitle = intent.getStringExtra(NotificationActivity.NOTIFICATION_TITLE)
            Navigation.toNotificationActivity(context, ScheduleRenderModel(title = scheduleTitle))
        }
    }
}

fun MutableList<PendingIntent>.cancelAll() {
    clear()
    forEach {it.cancel() }
}