package org.myspecialway.ui.navigation

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_navigation_locations.view.*
import kotlinx.android.synthetic.main.agenda_item.view.*
import kotlinx.android.synthetic.main.location_item.view.*
import org.myspecialway.R
import org.myspecialway.common.inflate
import org.myspecialway.ui.agenda.Location

class LocationAdapter(val locations: List<Location>, val selectedLocations: List<Location>, val clickListener: (Location) -> Unit, val longClickListener: (Location) -> Boolean) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return locations.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(parent.inflate(R.layout.location_item))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LocationViewHolder).bind(locations[position], selectedLocations, clickListener, longClickListener)
    }
}

class LocationViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(location: Location, selectedLocations: List<Location>, clickListener: (Location) -> Unit, longClickListener: (Location) -> Boolean) {
        itemView.location_text.text = location.name
        //itemView.location_image.setImageDrawable(location.image);
        itemView.setOnClickListener { clickListener(location) }
        itemView.setOnLongClickListener { longClickListener(location) }

        when (location.pinned) {
            true -> itemView.pin_image.visibility = View.VISIBLE
            false -> itemView.pin_image.visibility = View.INVISIBLE
        }

        when (selectedLocations.contains(location)) {
            true -> itemView.location_card_view.setBackgroundResource(R.drawable.border)
            false -> itemView.location_card_view.setBackgroundResource(R.drawable.border_white)
        }
    }
}