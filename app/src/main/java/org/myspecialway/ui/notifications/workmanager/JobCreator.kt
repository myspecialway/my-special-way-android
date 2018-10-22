package org.myspecialway.ui.notifications.workmanager

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class JobCreator : JobCreator {
    override fun create(tag: String): Job? = when(tag) {
        AlarmJob.ALARM_JOB_TAG -> AlarmJob()
        else -> null
    }
}