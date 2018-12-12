package org.myspecialway.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.myspecialway.R
import org.myspecialway.ui.navigation.NavigationLocationsActivity

class SettingsActivity : AppCompatActivity() {

    var context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings_layout)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment.newInstance(intent.getStringExtra(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY)))
                .commit()
    }

    fun onBackClick(view: View) {
        finish()
    }
}