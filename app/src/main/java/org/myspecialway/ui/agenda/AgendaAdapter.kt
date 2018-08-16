package org.myspecialway.ui.agenda

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.agenda_item.view.*
import org.myspecialway.R
import org.myspecialway.common.inflate
import kotlin.properties.Delegates

class AgendaAdapter(val onClick: (AgendaRenderModel) -> Unit ) : RecyclerView.Adapter<AgendaAdapter.AgendaHolder>() {

    var list: List<AgendaRenderModel> by Delegates.observable(listOf()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaHolder = AgendaHolder(parent)

    override fun onBindViewHolder(holder: AgendaAdapter.AgendaHolder, position: Int) { holder.bind(list[position]) }

    override fun getItemCount(): Int = list.size

    inner class AgendaHolder(view: ViewGroup) : RecyclerView.ViewHolder(view.inflate(R.layout.agenda_item)) {
        fun bind(render: AgendaRenderModel) {
            itemView.agenda_text.text = render.title
            itemView.agenda_icon.setBackgroundResource(R.drawable.sun)
            itemView.time.text = render.time?.timeDisplay
            itemView.setOnClickListener { onClick.invoke(render) }
        }
    }
}