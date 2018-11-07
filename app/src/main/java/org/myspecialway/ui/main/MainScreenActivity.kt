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
import org.myspecialway.common.handleBatteryManagement
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.ui.agenda.FIRST_TIME
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.scheduleAlarmOfAlarms
import org.myspecialway.ui.alarms.scheduleDailyAlarms
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.shared.AgendaViewModel

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val sp: SharedPreferences by inject()
    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        handleBatteryManagement()

        viewModel.getDailySchedule()

        clickListeners()
        render()
    }


    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
//        navButton.setOnClickListener { showNavigationDialog(this) }
        settings.setOnClickListener { Navigation.toSettingsActivity(this) }

    }

    override fun render() {
        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is AgendaState.CurrentSchedule -> {
                    schedule = state.schedule
                    scheduleName.text = state.schedule.title
                }
                is AgendaState.ListState -> {

                    scheduleAlarmOfAlarms()

                    scheduleTodayAlarmsSingleFirstTime()

                    scheduleName.visibility = View.VISIBLE
                }
                is AgendaState.Progress -> progress.visibility = state.progress
                is AgendaState.Failure -> handleError()
            }
        })
    }

    private fun scheduleTodayAlarmsSingleFirstTime() {
        if (sp.getBoolean(FIRST_TIME, true)) {
            scheduleDailyAlarms()
            sp.edit().putBoolean(FIRST_TIME, false).apply()
        }
    }

    private fun handleError() {
        scheduleName.visibility = View.VISIBLE
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

}