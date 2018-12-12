package org.myspecialway.ui.alarms

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.myspecialway.common.getRemainingAlarmsForToday
import org.myspecialway.common.getRemindersForToday
import org.myspecialway.data.local.Database
import org.myspecialway.ui.agenda.ScheduleModel
import org.myspecialway.ui.agenda.mapReminderRenderModel
import org.myspecialway.ui.agenda.mapScheduleRenderModel
import org.myspecialway.utils.Logger
import java.util.*

private const val TAG = "AlarmReceiver"
class AlarmsReceiver : BroadcastReceiver() {
    @SuppressLint("CheckResult")
    override fun onReceive(context: Context?, intent: Intent?) {

        Logger.d(TAG, "onReceive, scheduling alarms for schedules and reminders")
        val list = getLocalSchedule(context)

        // get the list of schedule
        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data.student.schedule }
                .subscribe({ scheduleList ->

                    val render = scheduleList
                            .map { mapScheduleRenderModel(it) }

                    val remainingAlarms = render.toMutableList()
                            .getRemainingAlarmsForToday()

                    AlarmJob.scheduleJobs(remainingAlarms)
                }, { })

        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data.student.reminder }
                .subscribe({ reminderList ->

                    val render = reminderList
                            ?.map { mapReminderRenderModel(it) }

                    val mutableList = render?.toMutableList()
                    val remainingAlarms = mutableList?.getRemindersForToday()

                    AlarmJob.scheduleReminderJobs(remainingAlarms)
                }, { })
    }

    private fun getLocalSchedule(context: Context?): Single<ScheduleModel> =
            Room.databaseBuilder(context!!, Database::class.java, "database")
                    .build()
                    .localDataSourceDAO()
                    .loadSchedule()

    companion object {
        fun getHourOfDay(hour: Int): Calendar {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            return calendar
        }
    }
}