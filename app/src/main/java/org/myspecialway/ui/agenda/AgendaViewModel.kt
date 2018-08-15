package org.myspecialway.ui.agenda

import android.arch.lifecycle.MutableLiveData
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with
import org.myspecialway.schedule.gateway.ScheduleResponse

class AgendaViewModel(private val repository: AgendaRepository,
                      private val scheduler: SchedulerProvider) : AbstractViewModel() {

    val uiData = MutableLiveData<List<ScheduleResponse.Schedule>>()

    init {
        getDailySchedule()
    }

    private fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(scheduler)
                .map { it.data.classById.schedule }
                .subscribe({
                    uiData.value = it
                }, { handleFailure(it) })
    }
}