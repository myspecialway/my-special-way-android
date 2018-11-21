package org.myspecialway.ui.navigation

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_navigation_locations.*
import kotlinx.android.synthetic.main.agenda_last_item.*
import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.LocationConverter

class NavigationLocationsActivity : AppCompatActivity() {
    private enum class State {
        BASIC, PINNING, UNPINNING
    }

    private lateinit var adapter: LocationAdapter
    private lateinit var locations : List<Location>
    private var selectedLocations = mutableListOf<Location>()
    private var state = State.BASIC

    companion object  {
        val LOCATIONS_PAYLOAD_KEY = "location_payload_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_locations)

        val locationsJson = intent.getStringExtra(LOCATIONS_PAYLOAD_KEY)
        locations = LocationConverter().fromString(locationsJson)

        initList()
    }

    private fun changeToolbar() {
        when (state) {
            State.BASIC -> {
                locationsTitle.text = getString(R.string.navigation_destinations_title)
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                backImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back))
                pinImage.visibility = View.INVISIBLE
            }

            State.PINNING -> {
                locationsTitle.text = ""
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.navLocationPinTitleBarBg))
                backImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back_white))
                pinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pin_white))
                pinImage.visibility = View.VISIBLE
                pinImage.setOnClickListener {
                    for (location: Location in selectedLocations) {
                        location.pinned = true
                    }

                    state = State.BASIC
                    changeToolbar()
                    selectedLocations.clear()
                    initList()

                }
            }

            State.UNPINNING -> {
                locationsTitle.text = ""
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.navLocationPinTitleBarBg))
                backImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back_white))
                pinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unpin))
                pinImage.visibility = View.VISIBLE
                pinImage.setOnClickListener {
                    for (location: Location in selectedLocations) {
                        location.pinned = false
                    }

                    state = State.BASIC
                    changeToolbar()
                    selectedLocations.clear()
                    initList()
                }
            }
        }
    }

    private fun initList() {
        locations = locations.sortedWith(CompareLocations)
        locationsRecyclerView.layoutManager = LinearLayoutManager(this)
        locationsRecyclerView.hasFixedSize()
        adapter = LocationAdapter(locations, selectedLocations, ::onClick, ::onLongClick)
        locationsRecyclerView.adapter = adapter
    }

    class CompareLocations {
        companion object : Comparator<Location> {
            override fun compare(a: Location, b: Location): Int = when {
                a.pinned && !b.pinned -> -1
                !a.pinned && b.pinned -> 1
                (a.name != null && b.name != null) -> a.name.compareTo(b.name)
                a.name == null -> 1
                else -> -1
            }
        }
    }

    fun onBackClick(view: View) {
        finish()
    }

    fun onClick(location: Location) {
        when (state) {
            State.BASIC -> {
                Navigation.toUnityNavigation(this, location.locationId!!)
            }

            State.PINNING, State.UNPINNING -> {
                if (selectedLocations.contains(location)) {
                    selectedLocations.remove(location)

                    if (selectedLocations.isEmpty()) {
                        state = State.BASIC
                        changeToolbar()
                    }
                } else {
                    selectedLocations.add(location)
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    fun onLongClick(location: Location): Boolean {
        when (state) {
            State.BASIC -> {
                if (location.pinned) {
                    state = State.UNPINNING
                } else {
                    state = State.PINNING
                }

                changeToolbar()
                selectedLocations.add(location)
            }

            State.PINNING, State.UNPINNING -> {
                if (selectedLocations.contains(location)) {
                    selectedLocations.remove(location)

                    if (selectedLocations.isEmpty()) {
                        state = State.BASIC
                        changeToolbar()
                    }
                } else {
                    selectedLocations.add(location)
                }
            }
        }

        adapter.notifyDataSetChanged()

        return true
    }
}
