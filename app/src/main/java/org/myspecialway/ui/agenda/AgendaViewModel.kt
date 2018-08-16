package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData

import org.myspecialway.R
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import java.util.*

class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val uiData = MutableLiveData<List<ScheduleRenderModel>>()

    init {
        getDailySchedule()
    }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .map { it.data.classById.schedule }
                .toObservable()
                .flatMapIterable { it }
                .map { agendaMapper(it) }
                .toList()
                .subscribe({
                    uiData.value = it
                }, { handleFailure(it) })
    }

    private fun agendaMapper(schedule: Schedule) = ScheduleRenderModel().apply {
        val currentTime = Calendar.getInstance(TimeZone.getDefault()).time

        title = schedule.lesson.title
        image = R.drawable.sun
        time = DateIndex.convertTimeFromIndex(schedule.index)
        isNow = currentTime.after(time?.date) && currentTime.before(addHour(time!!.date))
    }

    private fun addHour(currentTime: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = currentTime
        cal.add(Calendar.HOUR_OF_DAY, 1)
        return cal.time
    }
}

