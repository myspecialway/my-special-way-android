package org.myspecialway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Alarm(private val context: Context) {
    private var am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var pendingIntent: PendingIntent? = null

    fun scheduleAlarm(secondsFromNow: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerAtMillis = System.currentTimeMillis() + secondsFromNow * 1000

        am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    fun cancelAlarm() {
        if (pendingIntent != null) {
            am.cancel(pendingIntent)
        }
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val intent1 = Intent(context, NotificationActivity::class.java)
            intent1.putExtra(NotificationActivity.MESSAGE_TEXT, "בוקר טוב, זמן לשיעור חשבון!")
            context.startActivity(intent1)
        }
    }
}
