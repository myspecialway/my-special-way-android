package org.myspecialway.ui.agenda

import org.myspecialway.common.ViewType


// State
sealed class AgendaState {
    data class ListState(val scheduleList: List<ViewType>) : AgendaState()
    data class InActiveState(val messages: String) : AgendaState()
    data class CurrentSchedule(val schedule: ScheduleRenderModel, val position: Int) : AgendaState()
    data class LocationDataState(val list: List<Location>) : AgendaState()
    data class Failure(val throwable: Throwable) : AgendaState()
    data class Progress(val progress: Int) : AgendaState()
}