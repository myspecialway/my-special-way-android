package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.rxkotlin.subscribeBy
import org.myspecialway.R
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.addHour
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
                .toFlowable()
                .subscribeBy(
                        onNext = { subscribe(it) },
                        onError = { failure(it) },
                        onComplete = { println("Complete") }
                )
    }

    private fun subscribe(list: MutableList<ScheduleRenderModel>) {
        val today = getTodaySchedule(list)
        activateAlarmNextHours(today)
        listDataReady.value = today
    }

    private fun getTodaySchedule(list: MutableList<ScheduleRenderModel>) =
            list.filter { AgendaIndex.todayWeekIndex() == it.time?.dayDisplay }
                    .distinctBy { it.title }

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
        isNow = currentTime.after(time?.date) && currentTime.before(time!!.date.addHour(1))
    }


}
