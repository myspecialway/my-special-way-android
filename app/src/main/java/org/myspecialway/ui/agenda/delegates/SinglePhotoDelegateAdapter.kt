package org.myspecialway.ui.agenda.delegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.agenda_last_item.view.*
import org.myspecialway.R
import org.myspecialway.common.ViewType
import org.myspecialway.common.ViewTypeDelegateAdapter
import org.myspecialway.common.inflate
import org.myspecialway.common.load
import org.myspecialway.ui.agenda.SingleImageRes

class SinglePhotoDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = SingleImageHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as SingleImageHolder).bind(item as SingleImageRes)
    }

    inner class SingleImageHolder(view: ViewGroup) :
            RecyclerView.ViewHolder(view.inflate(R.layout.agenda_last_item)) {
        fun bind(data : SingleImageRes) = with(itemView) {
            image.load(data.image  )
        }
    }
}