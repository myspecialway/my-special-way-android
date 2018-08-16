package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData
import org.myspecialway.R
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with

class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val uiData = MutableLiveData<List<AgendaRenderModel>>()

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

    private fun agendaMapper(schedule: Schedule) = AgendaRenderModel().apply {
        title = schedule.lesson.title
        image = R.drawable.sun
        time = DateIndex.convertTimeFromIndex(schedule.index)
    }
}