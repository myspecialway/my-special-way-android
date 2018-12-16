package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_medicine_reminder.*
import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.sounds.SoundNotifications
import org.myspecialway.ui.agenda.ReminderType

class MedicineReminderActivity : Activity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_medicine_reminder)

        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.7F)

        lockedButton.setOnClickListener {
            Navigation.toNavigationPassword(this)
            finish()
        }

        initReminderNotification()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initReminderNotification()
    }

    private fun initReminderNotification() {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val reminderType = ReminderType.byName(intent.getStringExtra(REMINDER_TYPE_KEY))

        var soundRes: Int = 0
        when (reminderType) {
            ReminderType.MEDICINE -> {
                soundRes = R.raw.time_to_take_medicine
            }
            else -> {
                soundRes = 0
            }
        }

        val launchedFromHistory = intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0
        if (soundRes != 0 && !launchedFromHistory) SoundNotifications.playSoundNotification(  this, soundRes)


        setMaxAlarmExpDate()
        reminderText.text = notificationTitle
    }

    private fun setMaxAlarmExpDate() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            finish()
        }, FIFTEEN_MINUTE)
    }

    companion object {
        const val FIFTEEN_MINUTE: Long = 60000 * 15 // 15 min
        const val REMINDER_TYPE_KEY = "reminder_type_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}