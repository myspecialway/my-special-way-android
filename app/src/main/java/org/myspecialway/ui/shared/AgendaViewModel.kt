package org.myspecialway.ui.shared

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.rxkotlin.subscribeBy

import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.addHour
import org.myspecialway.common.with

import org.myspecialway.common.*
import org.myspecialway.ui.agenda.AgendaIndex
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.Schedule
import org.myspecialway.ui.agenda.ScheduleRenderModel

import java.util.*

// State
sealed class AgendaData

data class ListData(val scheduleList: List<ViewType>) : AgendaData()
data class Alarms(val list: List<ScheduleRenderModel>) : AgendaData()
data class CurrentSchedule(val schedule: ScheduleRenderModel, val position: Int) : AgendaData()
data class LocationData(val list: List<Location>) : AgendaData()


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
                .doOnNext { agendaLive.value = LocationData(it.data.locations) }
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
        activateAlarmNextHours(today.reversed())
        agendaLive.value = ListData(today.reversed())
    }

    private fun getTodaySchedule(list: MutableList<ScheduleRenderModel>) =
            list.filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                    .distinctBy { it.index }


    private fun activateAlarmNextHours(list: List<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    agendaLive.value = CurrentSchedule(scheduleRenderModel, index)
                    agendaLive.value = Alarms(getAlarms(list, index))
                }
            }

    private fun getAlarms(list: List<ScheduleRenderModel>, index: Int) =
            list.slice(IntRange(index + 1, list.size - 2))

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel()
            .apply {
                val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
                index = schedule.index
                title = schedule.lesson.title
                image = schedule.lesson.icon
                time = schedule.index.let { AgendaIndex.convertTimeFromIndex(it) }
                isNow = currentTime.after(time?.date) && currentTime.before(time!!.date.addHour(1))

            }
}
