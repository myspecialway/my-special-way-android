package org.myspecialway.ui.agenda.delegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.agenda_item.view.*
import org.myspecialway.R
import org.myspecialway.common.ViewType
import org.myspecialway.common.ViewTypeDelegateAdapter
import org.myspecialway.common.inflate
import org.myspecialway.common.load
import org.myspecialway.di.RemoteProperties
import org.myspecialway.ui.agenda.ScheduleRenderModel

class AgendaItemDelegateAdapter(val onClick: (ScheduleRenderModel) -> Unit) : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = AgendaHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as AgendaHolder).bind(item as ScheduleRenderModel)
    }

    inner class AgendaHolder(view: ViewGroup) : RecyclerView.ViewHolder(view.inflate(R.layout.agenda_item)) {
        fun bind(schedule: ScheduleRenderModel) {
            // set's the title
            itemView.agenda_text.text = schedule.title
            itemView.agenda_icon.load("${RemoteProperties.BASE_URL}lessons-icons/${schedule.image}.png")
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