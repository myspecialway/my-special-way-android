package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.media.MediaCas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.myspecialway.R
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.notifications.NotificationAlarmManager
import org.myspecialway.session.SessionManager
import org.myspecialway.ui.agenda.AgendaViewModel
import org.myspecialway.ui.agenda.ScheduleRenderModel

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val notificationAlarmManager: NotificationAlarmManager by inject()
    private val sessionManager: SessionManager by inject()

    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        clickListeners()
        render()
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        navButton.setOnClickListener { Navigation.toUnityNavigation(this, schedule) }
    }

    override fun render() {
        /**`
         * observe the user name
         */
        userDisplayName.text = sessionManager.getUserModel().fullName()

        /**
         * observe all the alarms we need to trigger and pass them to the alarms manager
         */
        viewModel.alarms.observe(this, Observer { it?.forEach { notificationAlarmManager.scheduleAlarm(it) } })

        /**
         * observe the current schedule title
         */
        viewModel.currentSchedule.observe(this, Observer {
            schedule = it!!
            scheduleName.text = it.title
        })

        /**
         * observe the list of data when it's ready
         */
        viewModel.listDataReady.observe(this, Observer { scheduleName.visibility = View.VISIBLE })

        /**
         * observe errors
         */
        viewModel.failure.observe(this, Observer { handleError() })

        /**
         * observe the progress bar
         */
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })
    }

    private fun handleError() {
        scheduleName.visibility = View.VISIBLE
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}