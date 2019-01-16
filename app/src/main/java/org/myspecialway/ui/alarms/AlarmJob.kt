package org.myspecialway.ui.alarms

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.gson.Gson
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ReminderType
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.JobCreator.Companion.ALARM_REMINDER_JOB_TAG
import org.myspecialway.ui.alarms.JobCreator.Companion.ALARM_SCHEDULE_JOB_TAG
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.utils.Logger
import java.util.*

private const val TAG = "AlarmJob"

class AlarmJob() : Job() {


    private val ALERTS_KEY = "alerts"
    private val SOUND_KEY = "sound"

    override fun onRunJob(params: Params): Result {
        val current = getBundle(params, ALARM_CURRENT)
        val previous = getBundle(params, ALARM_PREVIOUS)
        val reminderType = ReminderType.byName(params.extras.getString(REMINDER_TYPE, ReminderType.SCHEDULE.name))
        val notificationTime = params.extras.getLong(NOTIFICATION_TIME, 0L)
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val alerts = sp.getBoolean(ALERTS_KEY, true)
        val sounds = sp.getBoolean(SOUND_KEY, true)

        if (System.currentTimeMillis() - notificationTime > NotificationActivity.FIFTEEN_MINUTE){
            Logger.d(TAG, "too old notification. will not show to user. target time: " + Date(notificationTime) + ", " + reminderType)
            return Result.SUCCESS
        }

        if (!alerts) {
            Logger.d(TAG, "settings.alerts is turned off. do not show notification of type " + reminderType)
            return Result.SUCCESS
        }

        if (reminderType == ReminderType.MEDICINE) {
            Navigation.toMedicineReminderActivity(context, sounds)
        } else {
            Navigation.toNotificationActivity(context, current, previous, reminderType, sounds)
        }

        return Result.SUCCESS
    }

    companion object {
        const val ALARM_CURRENT = "alarm_current"
        const val ALARM_PREVIOUS = "alarm_previous"
        const val REMINDER_TYPE = "reminder_type"
        const val NOTIFICATION_TIME = "notification_time"

        fun scheduleJobs(alarms: List<ScheduleRenderModel>) {

            if (alarms.isEmpty()) {
                Logger.d(TAG, "No Schedules to schedule notification for")
                return
            }
            // cancel all previous jobs
            JobManager.instance().cancelAllForTag(ALARM_SCHEDULE_JOB_TAG)

            var previous = ScheduleRenderModel()
            val last = alarms.last()

            alarms.forEachIndexed { index, current ->
                current.isLast = last == current

                val currentIndex = alarms.indexOf(current)
                previous = getPrevious(index, previous, alarms, currentIndex)
                val notificationTime = current.time!!.date.time
                val timeTarget = notificationTime - System.currentTimeMillis()

                val extras = PersistableBundleCompat()
                extras.putString(ALARM_CURRENT, Gson().toJson(current))
                extras.putString(ALARM_PREVIOUS, Gson().toJson(previous))
                extras.putString(REMINDER_TYPE, ReminderType.SCHEDULE.name)
                extras.putLong(NOTIFICATION_TIME, notificationTime)

                Logger.d(TAG, "Scheduling notification of SCHEDULE, to " + current.time!!.date + ", name=" + current.title)

                JobRequest.Builder(ALARM_SCHEDULE_JOB_TAG)
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .addExtras(extras)
                        .setExact(timeTarget)
                        .build()
                        .schedule()
            }
        }

        fun scheduleReminderJobs(alarms: MutableList<Pair<Long, ReminderType>>?) {

            if (alarms == null || alarms.isEmpty()) {
                Logger.d(TAG, "No reminders to schedule")
                return
            }
            // cancel all previous jobs
            JobManager.instance().cancelAllForTag(ALARM_REMINDER_JOB_TAG)

            alarms.forEach {

                val extras = PersistableBundleCompat()
                extras.putString(REMINDER_TYPE, it.second.name)
                extras.putLong(NOTIFICATION_TIME, it.first)

                val timeTarget = it.first - System.currentTimeMillis()
                Logger.d(TAG, "Scheduling reminder of " + it.second + ", to " + Date(it.first))
                JobRequest.Builder(ALARM_REMINDER_JOB_TAG)
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .addExtras(extras)
                        .setExact(timeTarget)
                        .build()
                        .schedule()
            }
        }

        fun cancelAllJobs() {
            Logger.d(TAG, "Canceling all jobs")
            // cancel all schedule jobs
            JobManager.instance().cancelAllForTag(ALARM_SCHEDULE_JOB_TAG)
            // cancel all reminder jobs
            JobManager.instance().cancelAllForTag(ALARM_REMINDER_JOB_TAG)
        }

        private fun getPrevious(index: Int, previous: ScheduleRenderModel,
                                alarms: List<ScheduleRenderModel>,
                                currentIndex: Int): ScheduleRenderModel {
            var p = previous
            if (index > 0) {
                p = alarms[currentIndex - 1]
            }
            return p
        }
    }

    private fun getBundle(params: Params, key: String): ScheduleRenderModel? {
        val value = params.extras.getString(key, "")
        if (value == null) {
            return null
        }
        return Gson().fromJson<ScheduleRenderModel>(
                value,
                ScheduleRenderModel::class.java)
    }
}