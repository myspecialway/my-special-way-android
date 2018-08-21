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

    val uiData = MutableLiveData<List<ScheduleRenderModel>>()
    val alarm = MutableLiveData<List<ScheduleRenderModel>>()
    val progress = MutableLiveData<Int>()

    init {
        getDailySchedule()
    }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .map { it.data.classById.schedule }
                .flatMapIterable { it }
                .map { mapScheduleRenderModel(it) }
                .toList()
                .subscribe({
                    activateAlarmNextHours(it, 1)
                    // check
                    uiData.value = it.take(6)
                }, {
//                    handleFailure(it)
                })
    }

    private fun activateAlarmNextHours(it: MutableList<ScheduleRenderModel>, alarmAmount: Int) {
        it.take(6).forEachIndexed { index, scheduleRenderModel ->
            if (scheduleRenderModel.isNow) {
                // Get the x hours from now
                val nextHours = it.slice(IntRange(index + 1, it.size - 2)).take(alarmAmount)
                alarm.value = nextHours
                return@forEachIndexed
            }
        }
    }

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel().apply {
        val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
        title = schedule.lesson.title
        image = R.drawable.sun
        time = DateIndex.convertTimeFromIndex(schedule.index)
        isNow = currentTime.after(time?.date) && currentTime.before(addHour(time!!.date, 1))
    }

    private fun addHour(currentTime: Date, hours: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = currentTime
        cal.add(Calendar.HOUR_OF_DAY, hours)
        return cal.time
    }
}