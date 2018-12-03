package org.myspecialway.ui.notifications

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_medicine_reminder.*
import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ReminderType

class MedicineReminderActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_medicine_reminder)

        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val reminderType = ReminderType.byName(intent.getStringExtra(REMINDER_TYPE_KEY))
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.7F)

        setMaxAlarmExpDate()
//        finishOldAlarmIfNeeded(current)
         notificationText.text = notificationTitle
         lockedButton.setOnClickListener {
            Navigation.toNavigationPassword(this, "C_8")
            finish()
        }
    }

    private fun setMaxAlarmExpDate() {
        Handler().postDelayed({
            finish()
        }, FIFTEEN_MINUTE)
    }

    companion object {
        const val FIFTEEN_MINUTE: Long = 60000 * 15 // 15 min
        const val REMINDER_TYPE_KEY = "reminder_type_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}