package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.evernote.android.job.JobManager
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.notifications.androidjob.AlarmJob
import org.myspecialway.ui.notifications.androidjob.AlarmJob.Companion.ALARM_JOB_TAG
import org.myspecialway.ui.shared.AgendaViewModel
import org.myspecialway.ui.shared.Alarms
import org.myspecialway.ui.shared.CurrentSchedule
import org.myspecialway.ui.shared.ListData


class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
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
        settings.setOnClickListener { Navigation.toSettingsActivity(this) }

    }

    data class DialogModel(val name: String, val id: String)

    override fun render() {
        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.failure.observe(this, Observer { handleError() })
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })

        viewModel.agendaLive.observe(this, Observer { state->
            when(state) {
                is Alarms -> {
                    JobManager.instance().cancelAllForTag(ALARM_JOB_TAG)
                    AlarmJob.scheduleJobs(state.list)
                }
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