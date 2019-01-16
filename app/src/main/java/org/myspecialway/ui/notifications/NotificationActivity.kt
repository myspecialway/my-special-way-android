package org.myspecialway.ui.notifications

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.common.load
import org.myspecialway.di.RemoteProperties
import org.myspecialway.sounds.SoundNotifications
import org.myspecialway.ui.agenda.DestinationType
import org.myspecialway.ui.agenda.ReminderType
import org.myspecialway.ui.agenda.ScheduleRenderModel

class NotificationActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)
        getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.7F)

        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        initNotificationScreen()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initNotificationScreen()
    }

    private fun initNotificationScreen() {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val current = intent.getParcelableExtra<ScheduleRenderModel?>(SCHEDULE_CURRENT_KEY)
        val previous = intent.getParcelableExtra<ScheduleRenderModel?>(SCHEDULE_PREVIOUS_KEY)
        val reminderType = ReminderType.byName(intent.getStringExtra(REMINDER_TYPE_KEY))
        val playSoundNotifications =  intent.getBooleanExtra(SOUND_NOTIFICATIONS, false)


        setMaxAlarmExpDate()
        finishOldAlarmIfNeeded(current)
        // control if the navigate button will be shown
        navigationButton.visibility = if (reminderType != ReminderType.REHAB && (current?.unityDest == previous?.unityDest || current?.isLast ?: false)) {
            View.GONE
        } else {
            View.VISIBLE
        }

        notificationText.text = notificationTitle
        var soundRes: Int = 0
        when (reminderType) {
            ReminderType.MEDICINE -> {
                image.setImageResource(R.drawable.medicine)
                soundRes = R.raw.time_to_take_medicine
                window.setTitle(getString(R.string.medicine_reminder_activity_title))
            }
            ReminderType.REHAB -> {
                image.setImageResource(R.drawable.toilet)
                soundRes = R.raw.time_to_go_to_toilet
                window.setTitle(getString(R.string.rehab_reminder_activity_title))
            }
            ReminderType.SCHEDULE -> {
                val imageName = current?.image ?: ""
                val url = "${RemoteProperties.BASE_URL_IMAGES}${imageName}.png"
                image.load(url)
                window.setTitle(getString(R.string.notifiaction_activity_title))
            }
        }

        val launchedFromHistory = intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0
        if (playSoundNotifications && soundRes != 0 && !launchedFromHistory) SoundNotifications.playSoundNotification(this, soundRes)


        navigationButton.setOnClickListener {
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                keyguardManager?.requestDismissKeyguard(this, null)
            }

            when (reminderType) {
                ReminderType.MEDICINE -> {
                    navigateToUnityDest(current, DestinationType.MEDICINE)
                }
                ReminderType.REHAB -> {
                    navigateToNearestToilet()
                }
                ReminderType.SCHEDULE -> {
                    navigateToUnityDest(current, DestinationType.REGULAR)
                }
            }

            finish()
        }
    }

    private fun navigateToUnityDest(current: ScheduleRenderModel?, destinationType: DestinationType) {
        Navigation.toUnityNavigation(this, Gson().toJson(current?.unityDest), current?.image, destinationType)
    }

    private fun navigateToNearestToilet() {
        // todo: navigate to nearest toilet
        val destination = getString(org.myspecialway.R.string.toilet)

        Navigation.toUnityNavigation(this, destination, destination, DestinationType.TOILET)
    }

    private fun finishOldAlarmIfNeeded(currentAlarm: ScheduleRenderModel?) {
        // fifteen minutes ago time
        val fifteenAgo = System.currentTimeMillis() - FIFTEEN_MINUTE

        // if the alarm is before ten minutes ago destroy it
        if ((currentAlarm?.time?.date?.time) ?: Long.MAX_VALUE < fifteenAgo) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setMaxAlarmExpDate() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            finish()
        }, FIFTEEN_MINUTE)
    }

    companion object {
        const val FIFTEEN_MINUTE: Long = 60000 * 15 // 15 min
        const val SCHEDULE_CURRENT_KEY = "schedule_current_key"
        const val SCHEDULE_PREVIOUS_KEY = "schedule_previous_key"
        const val REMINDER_TYPE_KEY = "reminder_type_key"
        const val NOTIFICATION_TITLE = "notification_title"
        const val SOUND_NOTIFICATIONS = "sound_notifications"
    }
}