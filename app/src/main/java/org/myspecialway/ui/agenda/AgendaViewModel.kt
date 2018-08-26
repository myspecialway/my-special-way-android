package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.view.View
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import org.myspecialway.R
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import java.util.*

class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val listDataReady = MutableLiveData<List<ScheduleRenderModel>>()
    val alarms = MutableLiveData<List<ScheduleRenderModel>>()
    val currentSchedule = MutableLiveData<ScheduleRenderModel>()
    val currentSchedulePosition = MutableLiveData<Int>()

    init { getDailySchedule() }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .map { it.data.classById.schedule } // map the schedule list
                .flatMapIterable { it } // iterate on each element
                .map { mapScheduleRenderModel(it) } // map to render model
                .toList()

                //
                .subscribe()// back to list
//                .subscribe(::subscribe, ::failure)
    }

    private fun subscribe(list: MutableList<ScheduleRenderModel>) {
        val today =
                list.filter { AgendaIndex.todayWeekIndex() == it.time?.dayDisplay }
                .distinctBy { it.title }

        activateAlarmNextHours(today)
        listDataReady.value = today
    }

    private fun activateAlarmNextHours(list: List<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    currentSchedule.value = scheduleRenderModel
                    currentSchedulePosition.value = index
                    alarms.value = getAlarms(list, index)
                }
            }

    private fun getAlarms(list: List<ScheduleRenderModel>, index: Int) = list.slice(IntRange(index + 1, list.size - 2)).take(1)

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel().apply {
        val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
        title = schedule.lesson.title
        image = R.drawable.sun
        time = schedule.index.let { AgendaIndex.convertTimeFromIndex(it) }
        isNow = currentTime.after(time?.date) && currentTime.before(addHour(time!!.date, 1))
    }

    private fun addHour(currentTime: Date, hours: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = currentTime
        cal.add(Calendar.HOUR_OF_DAY, hours)
        return cal.time
    }
}
