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
        navButton.setOnClickListener { showNavigationDialog() }
        settings.setOnClickListener{ Navigation.toSettingsActivity(this)}
    }


    data class DialogModel(val name: String, val id: String)

    private fun showNavigationDialog() {
        val listItems = mutableListOf<DialogModel>().apply {
            add(DialogModel("כיתת סחלב", "B2"))
            add(DialogModel("כיתת פטל", "C1"))
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("נווט")
        builder.setSingleChoiceItems(listItems.map { it.name }.toTypedArray(), -1) { dialogInterface, i ->
            Navigation.toUnityNavigation(this, listItems[i].id)
            dialogInterface.dismiss()
        }
        // Set the neutral/cancel button click listener
        builder.setNeutralButton("בטל") { dialog, _ ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }


    override fun render() {
        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.failure.observe(this, Observer { handleError() })
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })

        viewModel.agendaLive.observe(this, Observer { agenda->
            when(agenda) {
                is Alarms -> agenda.list.forEach { notificationAlarmManager.scheduleAlarm(it) }
                is CurrentSchedule -> {
                    schedule = agenda.schedule
                    scheduleName.text = agenda.schedule.title
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