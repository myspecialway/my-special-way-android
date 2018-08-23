package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData
import android.view.View

import org.myspecialway.R
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import java.util.*

class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val listDataReady = MutableLiveData<List<ScheduleRenderModel>>()
    val alarm = MutableLiveData<List<ScheduleRenderModel>>()
    val currentSchedule = MutableLiveData<ScheduleRenderModel>()
    val currentSchedulePosition = MutableLiveData<Int>()

    init { getDailySchedule() }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .map { it.data.classById.schedule }
                .flatMapIterable { it }
                .map { mapScheduleRenderModel(it) }
                .toList()
                .subscribe(::subscribe, ::failure)
    }

    private fun subscribe(list: MutableList<ScheduleRenderModel>) {
        val today = list.take(6)
        activateAlarmNextHours(today.toMutableList())
        listDataReady.value = today
    }

    private fun activateAlarmNextHours(list: MutableList<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    currentSchedule.value = scheduleRenderModel
                    currentSchedulePosition.value = index
                    alarm.value = getAlarms(list, index)
                }
            }

    private fun getAlarms(list: MutableList<ScheduleRenderModel>, index: Int) = list.slice(IntRange(index + 1, list.size - 2))

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel().apply {
        val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
        title = schedule.lesson.title
        image = R.drawable.sun
        time = schedule.index.let { DateIndex.convertTimeFromIndex(it) }
        isNow = currentTime.after(time?.date) && currentTime.before(addHour(time!!.date, 1))
    }

    private fun addHour(currentTime: Date, hours: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = currentTime
        cal.add(Calendar.HOUR_OF_DAY, hours)
        return cal.time
    }
}