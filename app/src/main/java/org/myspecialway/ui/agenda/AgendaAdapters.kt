package org.myspecialway.ui.agenda

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.agenda_item.view.*
import kotlinx.android.synthetic.main.agenda_last_item.view.*
import org.myspecialway.R
import org.myspecialway.common.*
import org.myspecialway.di.RemoteProperties.TEMP


class AgendaAdapter(val onClick: (ScheduleRenderModel) -> Unit) : BaseDelegateAdapter() {
    init {
        delegateAdapters.put(AgendaTypes.ITEM_TYPE, AgendaItemDelegateAdapter { onClick.invoke(it) })
        delegateAdapters.put(AgendaTypes.SINGLE_IMAGE, SinglePhotoDelegateAdapter())
    }
}

class AgendaItemDelegateAdapter(val onClick: (ScheduleRenderModel) -> Unit) : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = AgendaHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as AgendaHolder).bind(item as ScheduleRenderModel)
    }

    inner class AgendaHolder(view: ViewGroup) : RecyclerView.ViewHolder(view.inflate(R.layout.agenda_item)) {
        fun bind(schedule: ScheduleRenderModel) {
            // set's the title
            itemView.agenda_text.text = schedule.title
            itemView.agenda_icon.load("${TEMP}lessons-icons/${schedule.image}.png")
            itemView.time.text = schedule.time?.timeDisplay

            drawBorderIfNeeded(schedule)

            itemView.setOnClickListener { onClick.invoke(schedule) }
        }

        private fun drawBorderIfNeeded(schedule: ScheduleRenderModel) {
            when(schedule.isNow){
                true -> itemView.card_view.setBackgroundResource(R.drawable.border)
                false -> itemView.card_view.setBackgroundResource(R.drawable.border_white)
            }
        }
    }
}

class SinglePhotoDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = SingleImageHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as SingleImageHolder).bind(item as SingleImageRes)
    }

    inner class SingleImageHolder(view: ViewGroup) : RecyclerView.ViewHolder(view.inflate(R.layout.agenda_last_item)) {
        fun bind(data : SingleImageRes) = with(itemView) {
            image.load(data.image  )
        }
    }
}