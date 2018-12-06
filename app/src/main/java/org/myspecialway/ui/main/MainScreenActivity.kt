package org.myspecialway.ui.main

//import org.myspecialway.ui.notifications.NotificationAlarmManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.Toast
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.common.enable
import org.myspecialway.ui.agenda.*
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.AlarmsReceiver
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.notifications.NotificationActivity
import org.myspecialway.ui.shared.AgendaViewModel
import java.util.*


class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val sp: SharedPreferences by inject()

    private val locationsSubject = BehaviorSubject.create<List<Location>>()

    private var schedule: ScheduleRenderModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        viewModel.getDailySchedule()
        viewModel.getLocations()
        activateAlarmsIfNeeded()
        clickListeners()
        render()
    }

    /**
     * this is a daily alarm [AlarmsReceiver] that will get called every day at 6 O'clock.
     * this alarm contain the logic of triggering al the daily alarms.
     */
    private fun activateAlarmOfAlarms(context: Context?) {
        val am: AlarmManager = getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmsReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        // launch alarms NOW for the first time.
        val now = Calendar.getInstance().time
        am.set(AlarmManager.RTC_WAKEUP, now.time, alarmIntent)

        // set repeating alarms for every day
        am.setRepeating(AlarmManager.RTC_WAKEUP, AlarmsReceiver.getHourOfDay(6).timeInMillis,
                AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        settings.setOnClickListener { Navigation.toSettingsActivity(this) }

        // listen to location events, if any then enable the navigation button and set the payload
        // on the click
        disposable = locationsSubject.subscribe({ navLocations ->
            navButton.enable(true)
            navButton.alpha = 1.0f
            navButton.setOnClickListener { Navigation.toNavigationPassword(this, navLocations) }
        }, {
            // set default nav params?
        })
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
                    activateAlarmsIfNeeded()
                    scheduleName.visibility = View.VISIBLE
                }
                is AgendaState.LocationDataState -> locationsSubject.onNext(state.list)
                is AgendaState.Progress -> progress.visibility = state.progress
                is AgendaState.Failure -> handleError()
            }
        })
    }

    private fun activateAlarmsIfNeeded() {
        activateAlarmOfAlarms(this)

   }

    private fun handleError() {
        scheduleName.visibility = View.VISIBLE
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

}