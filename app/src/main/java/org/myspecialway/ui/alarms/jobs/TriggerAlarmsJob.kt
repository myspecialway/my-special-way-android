package org.myspecialway.ui.alarms.jobs
import com.evernote.android.job.Job
import com.google.gson.Gson
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.AlarmHelper.ALARM_CURRENT
import org.myspecialway.ui.alarms.AlarmHelper.ALARM_PREVIOUS

class TriggerAlarmsJob : Job() {

    override fun onRunJob(params: Params): Result {
        val current = getBundle(params, ALARM_CURRENT)
        val previous = getBundle(params, ALARM_PREVIOUS)

        Navigation.toNotificationActivity(context, current, previous)

        return Result.SUCCESS
    }

    private fun getBundle(params: Params, key: String): ScheduleRenderModel {
        return Gson().fromJson<ScheduleRenderModel>(
                params.extras.getString(key, ""),
                ScheduleRenderModel::class.java)
    }
}