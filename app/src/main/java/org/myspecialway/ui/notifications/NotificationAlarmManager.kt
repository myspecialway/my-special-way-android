//package org.myspecialway.ui.notifications
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import org.myspecialway.common.Navigation
//import org.myspecialway.ui.agenda.ScheduleRenderModel
//
//class NotificationAlarmManager(private val context: Context) {
//    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//    private val alarmsQueue = mutableListOf<PendingIntent>()
//
//    fun setAlarms(alarms: List<ScheduleRenderModel>) {
//        alarmsQueue.cancelAll(alarmManager)
//
//
//        alarms.forEach { alarm ->
//            val intent = Intent(context, AlarmReceiver::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra(NotificationActivity.NOTIFICATION_TITLE, alarm.title)
//            val id = System.currentTimeMillis().toInt()
//            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT)
//            alarmsQueue.add(pendingIntent)
//            val triggerAtMillis = alarm.time!!.date.time - System.currentTimeMillis()
//            alarmManager.set(
//                    AlarmManager.RTC_WAKEUP,
//                    System.currentTimeMillis() + triggerAtMillis,
//                    pendingIntent
//            )
//        }
//
//    }
//
//    class AlarmReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val scheduleTitle = intent.getStringExtra(NotificationActivity.NOTIFICATION_TITLE)
//            Navigation.toNotificationActivity(context, ScheduleRenderModel(title = scheduleTitle))
//        }
//    }
//}
//
//fun MutableList<PendingIntent>.cancelAll(alarmManager: AlarmManager) {
//    forEach { alarmManager.cancel(it) }
//    clear()
//}