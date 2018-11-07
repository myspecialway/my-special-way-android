package org.myspecialway.ui.alarms
import android.app.job.JobScheduler
import com.evernote.android.job.DailyJob
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.gson.Gson
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.JobCreator.Companion.ALARM_JOB_TAG
import java.util.concurrent.TimeUnit

class AlarmJob : Job() {

    override fun onRunJob(params: Params): Result {
        val current = getBundle(params, ALARM_CURRENT)
        val previous = getBundle(params, ALARM_PREVIOUS)

        Navigation.toNotificationActivity(context, current, previous)

        return Result.SUCCESS
    }

    companion object {
        const val ALARM_CURRENT = "alarm_current"
        const val ALARM_PREVIOUS = "alarm_previous"

        fun scheduleJobs(alarms: List<ScheduleRenderModel>) {

            // cancel all previous jobs
            JobManager.instance().cancelAll()

            var previous = ScheduleRenderModel()
            val last = alarms.last()

            alarms.forEachIndexed { index, current ->
                current.isLast = last == current

                val currentIndex = alarms.indexOf(current)
                previous = getPrevious(index, previous, alarms, currentIndex)

                val extras = PersistableBundleCompat()
                extras.putString(ALARM_CURRENT, Gson().toJson(current))
                extras.putString(ALARM_PREVIOUS, Gson().toJson(previous))
                val timeTarget = current.time!!.date.time - System.currentTimeMillis()

                JobRequest.Builder(ALARM_JOB_TAG)
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .addExtras(extras)
                        .setExact(timeTarget)
                        .build()
                        .schedule()
            }
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

    private fun getBundle(params: Params, key: String): ScheduleRenderModel {
        return Gson().fromJson<ScheduleRenderModel>(
                params.extras.getString(key, ""),
                ScheduleRenderModel::class.java)
    }
}