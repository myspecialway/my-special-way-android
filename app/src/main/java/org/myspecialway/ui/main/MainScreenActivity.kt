package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import org.myspecialway.ui.shared.*


class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val notificationAlarmManager: NotificationAlarmManager by inject()
    private val sp: SharedPreferences by inject()

    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        viewModel.getDailySchedule()
        clickListeners()
        render()
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        navButton.setOnClickListener { showNavigationDialog(this) }
        settings.setOnClickListener { Navigation.toSettingsActivity(this) }

    }

    data class DialogModel(val name: String, val id: String)

    override fun render() {
        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is AgendaState.Alarms -> notificationAlarmManager.setAlarms(state.list)
                is AgendaState.CurrentSchedule -> {
                    schedule = state.schedule
                    scheduleName.text = state.schedule.title
                }
                is AgendaState.ListState -> scheduleName.visibility = View.VISIBLE
                is AgendaState.Progress -> progress.visibility = state.progress
                is AgendaState.Failure -> handleError()
            }
        })
    }

    private fun handleError() {
        scheduleName.visibility = View.VISIBLE
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}