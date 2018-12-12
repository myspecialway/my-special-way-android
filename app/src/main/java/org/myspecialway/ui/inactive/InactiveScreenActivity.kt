package org.myspecialway.ui.inactive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.myspecialway.R
import org.myspecialway.common.Navigation

class InactiveScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inactive_layout)
    }

    fun onSettingsClick(view: View) {
        Navigation.toSettingsActivity(this, "")
    }
}