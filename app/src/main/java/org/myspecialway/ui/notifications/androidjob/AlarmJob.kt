package org.myspecialway.ui.notifications.androidjob

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.gson.Gson
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel

class AlarmJob : Job() {

    override fun onRunJob(params: Params): Result {
        val schedule = Gson().fromJson<ScheduleRenderModel>(
                params.extras.getString(ALARM_PAYLOAD_KEY, ""),
                ScheduleRenderModel::class.java)

        Navigation.toNotificationActivity(context, schedule)

        return Result.SUCCESS
    }

    companion object {

        const val ALARM_JOB_TAG = "notification_job_tag"
        const val ALARM_PAYLOAD_KEY = "alarm_payload_key"

        fun scheduleJobs(alarms: List<ScheduleRenderModel>) =
                alarms.forEach { alarm ->
                    val extras = PersistableBundleCompat()
                    extras.putString(ALARM_PAYLOAD_KEY, Gson().toJson(alarm))
                    val timeTarget = alarm.time!!.date.time - System.currentTimeMillis()
                    JobRequest.Builder(ALARM_JOB_TAG)
                            .addExtras(extras)
                            .setExact(timeTarget)
                            .build()
                            .schedule()
                }
    }
}

