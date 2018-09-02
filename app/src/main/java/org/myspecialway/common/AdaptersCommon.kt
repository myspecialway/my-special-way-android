package org.myspecialway.common

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup



open class BaseDelegateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    var items: List<ViewType> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getViewType()
    }

    fun addData(items: List<ViewType>) {
        this.items = items
        notifyDataSetChanged()
    }

}

interface ViewType {
    fun getViewType() : Int
}

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}

