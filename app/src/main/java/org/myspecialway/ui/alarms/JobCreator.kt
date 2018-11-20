package org.myspecialway.ui.alarms

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class JobCreator : JobCreator {

    companion object {
        const val ALARM_JOB_TAG = "notification_job_tag"


    }

    override fun create(tag: String): Job? = when(tag) {
        ALARM_JOB_TAG -> AlarmJob()

        else -> null
    }
}