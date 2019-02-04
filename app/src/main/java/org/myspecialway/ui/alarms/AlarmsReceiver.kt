package org.myspecialway.ui.alarms

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinContext
import org.koin.standalone.StandAloneContext
import org.myspecialway.common.getRemainingAlarmsForToday
import org.myspecialway.common.getRemindersForToday
import org.myspecialway.data.local.Database
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.shared.AgendaRepository
import org.myspecialway.ui.shared.ImagesUtils
import org.myspecialway.utils.Logger
import java.util.*

private const val TAG = "AlarmReceiver"

private const val BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED"
private const val QUICKBOOT_POWERON_ACTION = "android.intent.action.QUICKBOOT_POWERON"
private const val TIME_SET_ACTION = "android.intent.action.TIME_SET"
private const val TIMEZONE_CHANGED_ACTION = "android.intent.action.TIMEZONE_CHANGED"
private const val HTC_QUICKBOOT_POWERON_ACTION = "com.htc.intent.action.QUICKBOOT_POWERON"
private val allowedActions = listOf(
        BOOT_COMPLETED_ACTION, QUICKBOOT_POWERON_ACTION, TIME_SET_ACTION, TIMEZONE_CHANGED_ACTION, HTC_QUICKBOOT_POWERON_ACTION,
        AlarmsReceiver.INTERNAL_ALARM_ACTION, AlarmsReceiver.INTERNAL_DAILY_ALARM_ACTION)

private var nonActiveTimesRenderModel = listOf<NonActiveTimeRenderModel>()

class AlarmsReceiver : BroadcastReceiver() {
    @SuppressLint("CheckResult")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!allowedActions.contains(intent?.action ?: "")) {
            return
        }

        if (AlarmsReceiver.INTERNAL_DAILY_ALARM_ACTION == intent?.action?: "") {

            // on the daily alarm first fetch data from server, so temporary changes that are not pushed to client are updated.
            Logger.d(TAG, "onReceive, fetch schedules from server before scheduling alarms")
            fetchSchedulesFromServerBeforeschedulingLocally(context)
        } else {
            scheduleAlarmsForSchedulesAndReminders(context)
        }
    }

    private fun fetchSchedulesFromServerBeforeschedulingLocally(context: Context?) {
        //instead of by inject, that is not possible in BroadcastReceiver
        val repository = (StandAloneContext.koinContext as KoinContext).get<AgendaRepository>()

        repository.getScheduleFromRemote()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
                    scheduleAlarmsForSchedulesAndReminders(context)
                }, {
                    Logger.d(TAG, "fetchSchedulesFromServerBeforeschedulingLocally, failed to fetch schedules from server. will use local copy. error: " + it)
                    // error means we didn't get the schedules from server, anyhow we want to schedule alarms from local.
                    scheduleAlarmsForSchedulesAndReminders(context)
                })
    }

    private fun scheduleAlarmsForSchedulesAndReminders(context: Context?) {
        Logger.d(TAG, "scheduleAlarmsForSchedulesAndReminders, scheduling alarms for schedules and reminders")
        val list = getLocalSchedule(context)


        // get the list of schedule
        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess { dataList -> nonActiveTimesRenderModel = getValidNonActiveTimes(dataList) }
                .map { it.data.student.schedule }
                .subscribe({ scheduleList ->

                    val render = scheduleList
                            .filter { !it.hours.isNullOrEmpty() }
                            .map { mapScheduleRenderModel(it) }

                    ImagesUtils.prefetchImages(render)

                    val remainingAlarms = render.toMutableList()
                            .getRemainingAlarmsForToday(nonActiveTimesRenderModel)

                    AlarmJob.scheduleJobs(remainingAlarms)
                }, { })

        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess { dataList -> nonActiveTimesRenderModel = getValidNonActiveTimes(dataList) } // if observers are always called in the same order, this line can be omitted.
                .map { it.data.student.reminder }
                .subscribe({ reminderList ->

                    val render = reminderList
                            ?.map { mapReminderRenderModel(it) }

                    val mutableList = render?.toMutableList()
                    val remainingAlarms = mutableList?.getRemindersForToday(nonActiveTimesRenderModel)

                    AlarmJob.scheduleReminderJobs(remainingAlarms)
                }, { })
    }

    private fun getValidNonActiveTimes(dataList: ScheduleModel): List<NonActiveTimeRenderModel> {
        val currentTime = System.currentTimeMillis()
        return dataList.data.student.nonActiveTimes
                .map { mapNonActiveTimeRenderModel(it) }
                .filter { it.startDateTime?.time ?: 0 > currentTime || it.endDateTime?.time ?: 0 > currentTime }
    }

    private fun getLocalSchedule(context: Context?): Single<ScheduleModel> =
            Room.databaseBuilder(context!!, Database::class.java, "database")
                    .build()
                    .localDataSourceDAO()
                    .loadSchedule()

    companion object {
        const val INTERNAL_ALARM_ACTION = "org.myspecialway.INTERNAL_ALARM"
        const val INTERNAL_DAILY_ALARM_ACTION = "org.myspecialway.INTERNAL_DAILY_ALARM"

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