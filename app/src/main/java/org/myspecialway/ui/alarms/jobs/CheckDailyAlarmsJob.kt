package org.myspecialway.ui.alarms.jobs

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import com.evernote.android.job.Job
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.myspecialway.common.getRemainingAlarmsForToday
import org.myspecialway.data.local.Database
import org.myspecialway.ui.agenda.mapScheduleRenderModel
import org.myspecialway.ui.alarms.AlarmHelper

/**
 * This job will run every day at 6:00 o'clock and grab the daily jobs, on each today alarm it will create
 * job to trigger, for example: alarm at 13:00 and alarm at 16:30
 */
class CheckDailyAlarmsJob : Job() {
    @SuppressLint("CheckResult")
    override fun onRunJob(params: Params): Result {

        val list = Room.databaseBuilder(context, Database::class.java, "database")
                .build()
                .localDataSourceDAO()
                .loadSchedule()

        // get the list of schedule
        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data.classById.schedule }
                .subscribe({ scheduleList ->

                    val render = scheduleList
                            .map { mapScheduleRenderModel(it) }

                    val remainingAlarms = render.toMutableList()
                            .getRemainingAlarmsForToday()

                    // create Alarm Job for each alarm based on the filter alarms for today
                    AlarmHelper.scheduleAlarms(remainingAlarms)
                }, {

                })

        return Result.SUCCESS
    }

}