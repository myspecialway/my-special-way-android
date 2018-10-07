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
import org.myspecialway.ui.agenda.Schedule
import org.myspecialway.ui.agenda.ScheduleRenderModel

import java.util.*

// State
sealed class AgendaViewModelState

data class ListViewModelState(val scheduleList: List<ViewType>) : AgendaViewModelState()
data class Alarms(val list: List<ScheduleRenderModel>) : AgendaViewModelState()
data class CurrentSchedule(val schedule: ScheduleRenderModel, val position: Int) : AgendaViewModelState()


class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val agendaLive = MutableLiveData<AgendaViewModelState>()

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
        agendaLive.value = ListViewModelState(today)
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

    /**
     * get the alarms from the next lesson and ignore the last one ( right now we [take] only the
     * next lesson)
     */
    private fun getAlarms(list: List<ScheduleRenderModel>, index: Int) =
            list.slice(IntRange(index + 1, list.size - 2)).take(1)

    private fun mapScheduleRenderModel(schedule: Schedule) = ScheduleRenderModel()
            .apply {

                val currentTime = Calendar.getInstance(TimeZone.getDefault()).time
                index = schedule.index
                title = schedule.lesson.title
                image = schedule.lesson.icon
                time = AgendaIndex.convertTimeFromIndex(schedule.index)
                isNow = currentTime.after(time?.date) && currentTime.before(time!!.date.addHour(1))
            }
}
