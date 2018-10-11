package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.notifications.NotificationAlarmManager

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val notificationAlarmManager: NotificationAlarmManager by inject()
    private val sp: SharedPreferences by inject()

    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        clickListeners()
        render()
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        navButton.setOnClickListener { showNavigationDialog(this) }
        settings.setOnClickListener{ Navigation.toSettingsActivity(this)}
    }


    override fun render() {
        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.failure.observe(this, Observer { handleError() })
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })

        viewModel.agendaLive.observe(this, Observer { state->
            when(state) {
                is Alarms -> notificationAlarmManager.setAlarms(state.list)
                is CurrentSchedule -> {
                    schedule = state.schedule
                    scheduleName.text = state.schedule.title
                }
                is ListData -> scheduleName.visibility = View.VISIBLE
            }
        })
    }

    private fun handleError() {
        scheduleName.visibility = View.VISIBLE
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}