package org.myspecialway.ui.settings

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.myspecialway.App
import org.myspecialway.R
import org.myspecialway.common.logout
import org.myspecialway.ui.login.showLogoutDialog
import android.widget.LinearLayout



class SettingsActivity : AppCompatActivity() {

    var context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (fragmentManager.findFragmentById(android.R.id.content) == null) {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, SettingsFragment()).commit()
        }

        //when logout is pressed
        if(intent?.action.equals("settings.logout")){
            showDialog()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val root = findViewById<View>(android.R.id.list).parent.parent as LinearLayout
        val toolbar = LayoutInflater.from(this).inflate(R.layout.toolbar, root, false) as Toolbar
        root.addView(toolbar, 0) // insert at top

        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.back)
    }


    private fun showDialog() {
        showLogoutDialog {
            approveButtonClickListener{
                App.instance?.applicationContext?.logout()
            }

            cancelButtonClickListener{
                dialog?.dismiss()
            }
        }
    }


    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
            val view = super.onCreateView(inflater, container, savedInstanceState)
            view.layoutDirection = View.LAYOUT_DIRECTION_RTL
            return view
        }

    }
}