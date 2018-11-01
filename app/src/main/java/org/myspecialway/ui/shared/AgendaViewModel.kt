package org.myspecialway.ui.shared

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.rxkotlin.subscribeBy
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import org.myspecialway.ui.agenda.*
import java.util.*


class AgendaViewModel(val repository: AgendaRepository,
                      val provider: SchedulerProvider) : AbstractViewModel() {

    val states = MutableLiveData<AgendaState>()


    fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(provider)
                .doOnSubscribe { states.value = AgendaState.Progress(View.VISIBLE) }
                .doFinally { states.value = AgendaState.Progress(View.GONE) }
                .map { it.data.classById.schedule } // map the schedule list
                .flatMapIterable { it } // iterate on each element
                .map { mapScheduleRenderModel(it) } // map to render model
                .toList()
                .map { filterTodayList(it) }
                .toFlowable()
                .subscribeBy(
                        onNext = { subscribe(it) },
                        onError = { states.value = AgendaState.Failure(it) }
                )
    }

    private fun subscribe(list: MutableList<ScheduleRenderModel>) {
        val today = getTodaySchedule(list)
        activateAlarmNextHours(today)
        states.value = AgendaState.Alarms(getAlarms(today))
        states.value = AgendaState.ListState(today)
    }

    private fun filterTodayList(list: MutableList<ScheduleRenderModel>) =
            list.asSequence()
                    .filter { AgendaIndex.todayWeekIndex(Calendar.getInstance()) == it.time?.dayDisplay }
                    .sortedBy {
                        it.index?.substringBefore("_")?.toInt()
                    }
                    .distinctBy { it.index }
                    .toList()


    private fun selectCurrentSchedule(list: List<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    states.value = AgendaState.CurrentSchedule(scheduleRenderModel, index)
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
                unityDest = schedule.location?.locationId ?: "C1"
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
