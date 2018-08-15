package org.myspecialway.ui.agenda

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.agenda_item.view.*
import org.myspecialway.R
import org.myspecialway.common.inflate
import org.myspecialway.schedule.gateway.ScheduleResponse
import kotlin.properties.Delegates

class AgendaAdapter(val onClick: (ScheduleResponse.Schedule) -> Unit) : RecyclerView.Adapter<AgendaAdapter.AgendaHolder>() {

    var list: List<ScheduleResponse.Schedule> by Delegates.observable(listOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaHolder = AgendaHolder(parent)

    override fun onBindViewHolder(holder: AgendaAdapter.AgendaHolder, position: Int) { holder.bind(list[position]) }

    override fun getItemCount(): Int = list.size

    inner class AgendaHolder(view: ViewGroup) : RecyclerView.ViewHolder(view.inflate(R.layout.agenda_item)) {
        fun bind(schedule: ScheduleResponse.Schedule) {
            itemView.agenda_text.text = schedule.lesson.title
            itemView.agenda_icon.setBackgroundResource(R.drawable.sun)
        }
    }
}