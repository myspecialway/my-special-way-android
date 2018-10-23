package org.myspecialway.ui.notifications.workmanager

import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import org.myspecialway.App
import org.myspecialway.TestActivity
import org.myspecialway.ui.agenda.ScheduleRenderModel

class AlarmJob : Job() {


    override fun onRunJob(params: Params): Result {
//        val schedule = Gson().fromJson<ScheduleRenderModel>(
//                params.extras.getString(ALARM_PAYLOAD_KEY, ""),
//                ScheduleRenderModel::class.java)
        val current = cachedAlarms.first()
        cachedAlarms.remove(current)

        if (cachedAlarms.isNotEmpty()) {
            JobRequest.Builder(ALARM_JOB_TAG)
                    .setExact(cachedAlarms.first())
                    .build()
                    .schedule()
        }


        context.startActivity(Intent(context, TestActivity::class.java))

//        Navigation.toNotificationActivity(context, current)

        return Result.SUCCESS
    }

    companion object {

        private lateinit var cachedAlarms: MutableList<Long>

        const val ALARM_JOB_TAG = "notification_job_tag"
        const val ALARM_PAYLOAD_KEY = "alarm_payload_key"

        val times = arrayListOf<Long>(5001, 5002, 5003, 5004)

        fun initScheduleJob(alarms: List<ScheduleRenderModel>) {
            // cache the alarms


            cachedAlarms = times

            JobRequest.Builder(ALARM_JOB_TAG)
                    .setExact(cachedAlarms.first())
                    .build()
                    .schedule()


        }


        //            val extras = PersistableBundleCompat()
//            extras.putString(ALARM_PAYLOAD_KEY, Gson().toJson(firstAlarm))
//                    .addExtras(extras)

//                times.forEach { alarm ->
//                    val extras = PersistableBundleCompat()
//                    extras.putString(ALARM_PAYLOAD_KEY, Gson().toJson(alarms[0]))
//                    val timeTarget = alarm.time!!.date.time - System.currentTimeMillis()
//                    val id = JobRequest.Builder(ALARM_JOB_TAG)
//                            .addExtras(extras)
//                            .setExact(alarm)
//                            .build()
//                            .schedule()
//
//                    Log.d("alarmmanagerlog", id.toString())
//                }
    }
}
