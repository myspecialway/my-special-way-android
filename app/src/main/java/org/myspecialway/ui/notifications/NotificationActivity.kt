package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.common.load
import org.myspecialway.ui.agenda.ReminderType
import org.myspecialway.ui.agenda.ScheduleRenderModel

class NotificationActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)

        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.7F)
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

        setMaxAlarmExpDate()
        finishOldAlarmIfNeeded(current)
        // control if the navigate button will be shown
        navigationButton.visibility = if (reminderType != ReminderType.REHAB && (current?.unityDest == previous?.unityDest || current?.isLast ?: false)) {
            View.GONE
        } else {
            View.VISIBLE
        }

        notificationText.text = notificationTitle
        when (reminderType) {
            ReminderType.MEDICINE -> image.setImageResource(R.drawable.medicine)
            ReminderType.REHAB -> image.setImageResource(R.drawable.toilet)
            ReminderType.SCHEDULE -> image.load(current?.image ?: "")
        }


        navigationButton.setOnClickListener {

            if (reminderType != ReminderType.REHAB) {
                navigateToNearestToilet(current)
            } else {
                navigateToUnityDest(current)
            }
            finish()
        }
    }

    private fun navigateToUnityDest(current: ScheduleRenderModel?) {
        Navigation.toUnityNavigation(this, current?.unityDest ?: "C_1")
    }

    private fun navigateToNearestToilet(current: ScheduleRenderModel?) {
        // todo: navigate to nearest toilet
        Navigation.toUnityNavigation(this, current?.unityDest ?: "C_1")
    }

    private fun finishOldAlarmIfNeeded(currentAlarm: ScheduleRenderModel?) {
        // fifteen minutes ago time
        val fifteenAgo = System.currentTimeMillis() - FIFTEEN_MINUTE

        // if the alarm is before ten minutes ago destroy it
        if((currentAlarm?.time?.date?.time) ?: Long.MAX_VALUE < fifteenAgo) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
    private fun setMaxAlarmExpDate() {
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
    }
}