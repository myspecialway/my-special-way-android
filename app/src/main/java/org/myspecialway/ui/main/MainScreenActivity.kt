package org.myspecialway.ui.main

//import org.myspecialway.ui.notifications.NotificationAlarmManager
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import org.myspecialway.common.load
import org.myspecialway.di.RemoteProperties
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.ui.agenda.Location
import org.myspecialway.ui.agenda.Reminder
import org.myspecialway.ui.agenda.ScheduleRenderModel
import org.myspecialway.ui.alarms.AlarmsReceiver
import org.myspecialway.ui.login.UserModel
import org.myspecialway.ui.shared.AgendaViewModel
import org.myspecialway.utils.Logger
import java.util.*

private const val TAG = "MainScreenActivity"

class MainScreenActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private val sp: SharedPreferences by inject()

    private val locationsSubject = BehaviorSubject.create<List<Location>>()

    private var schedule: ScheduleRenderModel? = null

    @SuppressLint("BatteryLife")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        Logger.d(TAG, "MainScreen onCreate")
        render()

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
        clickListeners()
    }

    /**
     * this is a daily alarm [AlarmsReceiver] that will get called every day at 6 O'clock.
     * this alarm contain the logic of triggering all the daily alarms.
     */
    private fun activateAlarmOfAlarms(context: Context?) {
        val am: AlarmManager = getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(AlarmsReceiver.INTERNAL_ALARM_ACTION, null, context, AlarmsReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val sixAmTimeInMillis = AlarmsReceiver.getHourOfDay(6).timeInMillis

        // launch alarms NOW for the first time only if 6AM has  not arrived yet.
        // Otherwise, setRepeating will trigger the alarm now anyhow.
//        val now = Calendar.getInstance().time
//        am.set(AlarmManager.RTC_WAKEUP, now.time, alarmIntent)

        // send directly to the broadcast receiver, without alarm
        sendBroadcast(intent)


        // set repeating alarms for every day
        am.setRepeating(AlarmManager.RTC_WAKEUP, sixAmTimeInMillis,
                AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    private fun clickListeners() {
        scheduleButton.setOnClickListener { Navigation.toScheduleActivity(this) }
        // for testing medicine reminder screen
//        scheduleButton.setOnClickListener { Navigation.toMedicineReminderActivity(this) }
        // for testing toilet reminder screen
//        scheduleButton.setOnClickListener {Navigation.toNotificationActivity(this, null, null, ReminderType.REHAB)}

        // listen to location events, if any then enable the navigation button and set the payload
        // on the click
        disposable = locationsSubject.subscribe({ navLocations ->
            settings.setOnClickListener { Navigation.toNavigationPassword(this) }

            Navigation.navLocations = navLocations
            navButton.setOnClickListener { Navigation.toNavigationPassword(this) }
        }, {
            // set default nav params?
        })
    }

    override fun render() {

        val userDisplayNamePrefix = resources.getString(R.string.user_prefix_text)
        val userDisplayNameString = UserModel().getUser(sp).fullName()
        userDisplayName.text = "$userDisplayNamePrefix $userDisplayNameString"

        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is AgendaState.CurrentSchedule -> {
                    schedule = state.schedule
                    scheduleName.text = state.schedule.title
                    val schedualImage =  "${RemoteProperties.BASE_URL_IMAGES}${state.schedule.image}.png"
                    location_image.load(schedualImage)

                }
                is AgendaState.ListState -> {
                    activateAlarmOfAlarms(this)
                    scheduleName.visibility = View.VISIBLE
                }
                is AgendaState.InActiveState -> {
                    activateAlarmOfAlarms(this)
                    Navigation.toInActivity(this)
                    // todo: timer to cancel inActive mode.
                }
                is AgendaState.LocationDataState -> locationsSubject.onNext(state.list)
//                is AgendaState.Progress -> progress.visibility = state.progress
                is AgendaState.Failure -> handleError(state.throwable)
//                is AgendaState.RemindersState -> handleReminders(state?.reminders)
            }
        })
    }

    private fun handleReminders(reminders: List<Reminder>?) {
        Logger.d(TAG, "Handling reminders " + reminders)

        // nothing to do here as reminder are handled together with schedules
    }

    private fun handleError(throwable: Throwable) {
        scheduleName.visibility = View.VISIBLE
        Logger.e(TAG, "Error getting schedules and reminders", throwable)
        Toast.makeText(this, "Error " + throwable.message, Toast.LENGTH_SHORT).show()
    }

}