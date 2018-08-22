package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.myspecialway.R
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.notifications.Alarm
import org.myspecialway.ui.agenda.AgendaViewModel

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val alarmManager: Alarm by inject()
    private val currentClass: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        clickListeners()
        render()
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toSchduleActivity(this) }
        navButton.setOnClickListener { Navigation.toUnityNavigation(this, currentClass ?: "No Destination") }
    }

    override fun render() {
        /**
         * observe the user name
         */
        userDisplayName.text = sessionManager?.userData?.fullName()

        /**
         * observe all the alarms we need to trigger and pass them to the alarm manager
         */
        viewModel.alarm.observe(this, Observer { it?.forEach { alarmManager.scheduleAlarm(it) } })

        /**
         * observe the current schedule title
         */
        viewModel.currentSchedule.observe(this, Observer { scheduleName.text = it })

        /**
         * observe the list of data when it's ready
         */
        viewModel.listDataReady.observe(this, Observer { scheduleName.visibility = View.VISIBLE })

        /**
         * observe the progress bar
         */
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })
    }
}