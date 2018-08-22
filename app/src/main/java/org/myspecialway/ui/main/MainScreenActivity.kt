package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.myspecialway.R
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.myspecialway.common.BaseActivity
import org.myspecialway.notifications.Alarm
import org.myspecialway.ui.agenda.AgendaActivity
import org.myspecialway.ui.agenda.AgendaViewModel

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val alarmManager: Alarm by inject()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        clickListeners()
        render()
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener {
            startActivity(Intent(this@MainScreenActivity, AgendaActivity::class.java))
        }

        navButton.setOnClickListener {
            // start navigation in Unity app
            try {
                val intent = Intent()
                intent.component = ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity")
                intent.putExtra("destination", "D") //TODO - need to pass the real destination name

                startActivity(intent)
            } catch (e: Exception) {

                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun render() {
        viewModel.alarm.observe(this, Observer { it?.forEach { alarmManager.scheduleAlarm(it) } })
        viewModel.currentSchedule.observe(this, Observer { scheduleName.text = it?.title })
    }
}
