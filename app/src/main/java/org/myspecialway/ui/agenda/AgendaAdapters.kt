package org.myspecialway.ui.agenda

import org.myspecialway.common.BaseDelegateAdapter
import org.myspecialway.ui.agenda.delegates.AgendaItemDelegateAdapter
import org.myspecialway.ui.agenda.delegates.SinglePhotoDelegateAdapter


class AgendaAdapter(val onClick: (ScheduleRenderModel) -> Unit) : BaseDelegateAdapter() {
    init {
        delegateAdapters.put(AgendaTypes.ITEM_TYPE, AgendaItemDelegateAdapter { onClick.invoke(it) })
        delegateAdapters.put(AgendaTypes.SINGLE_IMAGE, SinglePhotoDelegateAdapter())
    }
}