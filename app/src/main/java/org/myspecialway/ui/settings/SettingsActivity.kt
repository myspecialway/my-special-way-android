package org.myspecialway.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.myspecialway.App
import org.myspecialway.common.logout
import org.myspecialway.ui.login.showLogoutDialog
import org.myspecialway.R

class SettingsActivity : AppCompatActivity() {

    var context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings_layout)
    }

    fun onLogoutClick(view: View) {
        showLogoutDialog {
            approveButtonClickListener {
                App.instance?.applicationContext?.logout()
            }

            cancelButtonClickListener {
                dialog?.dismiss()
            }
        }
    }

    fun onBackClick(view: View) {
        finish()
    }
}