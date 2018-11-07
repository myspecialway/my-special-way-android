package org.myspecialway.ui.alarms

import android.content.Context
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.gson.Gson
import org.myspecialway.ui.agenda.ScheduleRenderModel
import java.util.*

object AlarmHelper {
    const val ALARM_CURRENT = "alarm_current"
    const val ALARM_PREVIOUS = "alarm_previous"

    fun scheduleAlarms(alarms: List<ScheduleRenderModel>) {

        // cancel all previous jobs
        JobManager.instance().cancelAllForTag(JobCreator.TRIGGER_TODAY_ALARMS_JOB)

        var previous = ScheduleRenderModel()
        val last = alarms.last()

        alarms.forEachIndexed { index, current ->
            current.isLast = last == current

            val currentIndex = alarms.indexOf(current)
            previous = getPreviousSchedule(index, previous, alarms, currentIndex)

            // pass the current class and the before class to the target alarm activity
            val extras = PersistableBundleCompat()
            extras.putString(ALARM_CURRENT, Gson().toJson(current))
            extras.putString(ALARM_PREVIOUS, Gson().toJson(previous))
            val timeTarget = current.time!!.date.time - System.currentTimeMillis()

            JobRequest.Builder(JobCreator.TRIGGER_TODAY_ALARMS_JOB)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .addExtras(extras)
                    .setExact(timeTarget)
                    .build()
                    .schedule()
        }
    }

    /**
     * gets the schedule (class) that was before the current class.
     * currently used for checking if the current class is same as the before class.
     */
    private fun getPreviousSchedule(index: Int, previous: ScheduleRenderModel,
                                    alarms: List<ScheduleRenderModel>,
                                    currentIndex: Int): ScheduleRenderModel {
        var p = previous
        if (index > 0) {
            p = alarms[currentIndex - 1]
        }
        return p
    }


    fun getHourOfDay(hour: Int, min: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }

}

/**
 * Daily alarm that is getting trigger in the morning and trigger the daily alarms for every day.
 */
fun Context.scheduleAlarmOfAlarms() {
    // if there are daily jobs then abort.
    if (JobManager.instance().getAllJobRequestsForTag(JobCreator.CHECK_TODAY_ALARMS_JOB).isNotEmpty())
        return

    DailyJob.schedule(JobRequest.Builder(JobCreator.CHECK_TODAY_ALARMS_JOB),
            AlarmHelper.getHourOfDay(6, 0).timeInMillis,
            AlarmHelper.getHourOfDay(6, 1).timeInMillis)

}

/**
 *
 */
fun Context.scheduleDailyAlarms() {
    JobRequest.Builder(JobCreator.TRIGGER_TODAY_ALARMS_JOB)
            .startNow()
            .build()
            .schedule()
}

