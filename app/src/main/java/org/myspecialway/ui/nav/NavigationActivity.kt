package org.myspecialway.ui.nav

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.myspecialway.R

class NavigationActivity : AppCompatActivity() {

    companion object  {
        val LOCATIONS_PAYLOAD_KEY = "location_payload_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
    }
}
