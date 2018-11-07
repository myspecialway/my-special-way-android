package org.myspecialway.ui.alarms

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import org.myspecialway.ui.alarms.jobs.CheckDailyAlarmsJob
import org.myspecialway.ui.alarms.jobs.TriggerAlarmsJob

class JobCreator : JobCreator {

    companion object {
        // this job will trigger the daily alarms
        const val TRIGGER_TODAY_ALARMS_JOB = "trigger_alarm_job"

        // this job will check the daily jobs at 6 o'clock
        const val CHECK_TODAY_ALARMS_JOB = "check_alarm_job"


    }

    override fun create(tag: String): Job? = when(tag) {
        TRIGGER_TODAY_ALARMS_JOB -> TriggerAlarmsJob()
        CHECK_TODAY_ALARMS_JOB -> CheckDailyAlarmsJob()
        else -> null
    }
}