package org.myspecialway.ui.alarms

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.ajalt.timberkt.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.myspecialway.common.getRemainingAlarmsForToday
import org.myspecialway.data.local.LocalDataSource
import org.myspecialway.ui.agenda.mapScheduleRenderModel

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private var disposable: Disposable? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val list = get<LocalDataSource>().loadSchedule()

        // get the list of schedule
        disposable = list
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data.classById.schedule }
                .subscribe({ scheduleList ->
                    val render = scheduleList.map { mapScheduleRenderModel(it) }
                    val remainingAlarms = render.toMutableList().getRemainingAlarmsForToday()
                    AlarmJob.scheduleJobs(remainingAlarms)
                }, {
                    d { "can't get the schedule list" }
                })

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}