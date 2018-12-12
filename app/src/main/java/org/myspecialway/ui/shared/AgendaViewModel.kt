package org.myspecialway.ui.shared

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.view.View
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.filterTodayList
import org.myspecialway.common.with

import org.myspecialway.common.*
import org.myspecialway.ui.agenda.*

import org.myspecialway.ui.agenda.*


class AgendaViewModel(val repository: AgendaRepository,
                      val provider: SchedulerProvider) : AbstractViewModel() {

    val states = MutableLiveData<AgendaState>()

    fun getDailySchedule() = launch {
        repository.getSchedule()
                .with(provider)
                .doOnSubscribe { states.value = AgendaState.Progress(View.VISIBLE) }
                .doFinally { states.value = AgendaState.Progress(View.GONE) }
//                .doOnNext { states.value = AgendaState.RemindersState(it.data.student.reminder) }
                .map { it.data.student.schedule } // map the schedule list
                .flatMapIterable { it } // iterate on each element
                .map { mapScheduleRenderModel(it) } // map to render model
                .toList()
                .map { it.filterTodayList() }
                .toFlowable()
                .subscribeBy(
                        onNext = { subscribe(it.toMutableList()) },
                        onError = { states.value = AgendaState.Failure(it) }
                )
    }

    private fun subscribe(today: MutableList<ScheduleRenderModel>) {
        selectCurrentSchedule(today)

        if(isAppInActive(today.get(0).time, today.get(today.size-1).time)){
            states.value = AgendaState.InActiveState("of time")
            return
        }

        states.value = AgendaState.ListState(today)
    }

    private fun selectCurrentSchedule(list: List<ScheduleRenderModel>) =
            list.forEachIndexed { index, scheduleRenderModel ->
                if (scheduleRenderModel.isNow) {
                    states.value = AgendaState.CurrentSchedule(scheduleRenderModel, index)
                }
            }

    fun getLocations() = launch {
        repository.getLocations()
                .with(provider)
                .doOnSubscribe {
                    states.value = AgendaState.Progress(View.VISIBLE)
                }.doFinally {
                    states.value = AgendaState.Progress(View.GONE)
                }.map { it.data.locations }
                .subscribeBy(
                        onNext = { states.value = AgendaState.LocationDataState(it) },
                        onError = { states.value = AgendaState.Failure(it) }
                )
    }

    fun isAppInActive(agendaStartTime:Time?, agendaEndTime:Time?) : Boolean{
        val currentTime = Date(System.currentTimeMillis())

        if(agendaStartTime == null || agendaEndTime == null){
            return true;
        }

        return agendaStartTime.date.after(currentTime) || agendaEndTime.date.before(currentTime)
    }
}
