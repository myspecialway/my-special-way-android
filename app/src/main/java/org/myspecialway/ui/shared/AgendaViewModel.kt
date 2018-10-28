package org.myspecialway.ui.shared

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.rxkotlin.subscribeBy

import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with

import org.myspecialway.common.*
import org.myspecialway.ui.agenda.AgendaIndex
import org.myspecialway.ui.agenda.Schedule
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.agenda.createHour

import java.util.*

// State
sealed class AgendaData

data class ListData(val scheduleList: List<ViewType>) : AgendaData()
data class Alarms(val list: List<ScheduleRenderModel>) : AgendaData()
data class CurrentSchedule(val schedule: ScheduleRenderModel, val position: Int) : AgendaData()


class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val agendaLive = MutableLiveData<AgendaData>()

    init {
        getDailySchedule()
    }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .doOnSubscribe { progress.value = View.VISIBLE }
                .doFinally { progress.value = View.GONE }
                .map { it.data.classById.schedule } // map the schedule list
                .flatMapIterable { it } // iterate on each element
                .map { mapScheduleRenderModel(it) } // map to render model
                .toList()
                .toFlowable()
                .subscribeBy(
                        onNext = { subscribe(it) },
                        onError = { failure(it) }
                )
    }

    private fun subscribe(list: MutableList<ScheduleRenderModel>) {
        val today = getTodaySchedule(list)
        activateAlarmNextHours(today)
        agendaLive.value = Alarms(getAlarms(today))
        agendaLive.value = ListData(today)
    }

    private fun getTodaySchedule(list: MutableList<ScheduleRenderModel>) =
            list.asSequence()
                    .filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                    .sortedBy {
                        it.index?.substringBefore("_")?.toInt()
                    }
                    .distinctBy { it.index }
                    .toList()


    private fun activateAlarmNextHours(list: List<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    agendaLive.value = CurrentSchedule(scheduleRenderModel, index)
                }
            }

    private fun getAlarms(list: List<ScheduleRenderModel>) =
            list.filter { System.currentTimeMillis() < it.time!!.date.time }

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel()
            .apply {
                val display = schedule.hours ?: "7:30 - 08:00"
                val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
                index = schedule.index
                title = schedule.lesson.title
                this.hours = schedule.hours
                unityDest = schedule.location?.locationId ?: ""
                image = schedule.lesson.icon
                time = schedule.index.let { AgendaIndex.convertTimeFromIndex(it, display) }
                isNow = currentTime.after(time?.date) && currentTime.before(createHour(hour(display), min(display)))
            }

    private fun min(h: String): Int = h.substringAfter("-")
                .trim()
                .split(":")[1]
                .toInt()


    private fun hour(h: String): Int = h.substringAfter("-")
                .trim()
                .split(":")[0]
                .toInt()
}
