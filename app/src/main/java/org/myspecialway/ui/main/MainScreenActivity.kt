package org.myspecialway.ui.main

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.common.enable
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.notifications.NotificationAlarmManager
import org.myspecialway.ui.shared.*
import java.util.concurrent.TimeUnit


class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val notificationAlarmManager: NotificationAlarmManager by inject()
    private val sp: SharedPreferences by inject()

    private val locations = BehaviorSubject.create<List<Location>>()

    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        clickEvents()
        render()
    }

    private fun clickEvents() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        settings.setOnClickListener { Navigation.toSettingsActivity(this) }

        // listen to location events, if any then enable the navigation button and set the payload
        // on the click
        disposable = locations.subscribe({ navLocations ->
            navButton.enable(true)
            navButton.alpha = 1.0f
            navButton.setOnClickListener { Navigation.navigateLocationActivity(this, navLocations) }
        }, {
            // set default nav params?
        })
    }

    override fun render() {

        Handler().postDelayed({
            locations.onNext(mutableListOf<Location>().apply {
                add(Location(1, "D1", "פטל", false))
                add(Location(2, "D2", "פל", false))
                add(Location(3, "D3", "ל", false))
            })
        },3000)

        userDisplayName.text = UserModel().getUser(sp).fullName()

        viewModel.failure.observe(this, Observer { handleError() })
        viewModel.progress.observe(this, Observer { progress.visibility = it!! })

        viewModel.agendaLive.observe(this, Observer { state ->
            when (state) {
                is LocationData -> locations.onNext(state.list)
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