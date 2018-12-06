package org.myspecialway.ui.notifications

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.common.load
import org.myspecialway.ui.agenda.ScheduleRenderModel
class NotificationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)
        getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        val (notificationTitle, current, previous) = getBundle(intent)

        setMaxAlarmExpDate()
        finishOldAlarmIfNeeded(current)
        // control if the navigate button will be shown
        navigationButton.visibility = if (current.unityDest == previous.unityDest || current.isLast) {
            View.GONE
        } else {
            View.VISIBLE
        }

        notificationText.text = notificationTitle
        image.load(current.image ?: "")
        navigationButton.setOnClickListener {

            Navigation.toUnityNavigation(this, "C1")
            finish()
        }
    }

    private fun finishOldAlarmIfNeeded(currentAlarm: ScheduleRenderModel) {
        // ten minutes ago time
        val tenAgo = System.currentTimeMillis() - TEN_MINUTE

        // if the alarm is before ten minutes ago destroy it
        if(currentAlarm.time!!.date.time < tenAgo) {
            finish()
        }
    }

    private fun setMaxAlarmExpDate() {
        Handler().postDelayed({
            finish()
        }, TEN_MINUTE)
    }

    private fun getBundle(intent: Intent): Triple<String, ScheduleRenderModel, ScheduleRenderModel> {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val current = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_CURRENT_KEY)
        val previous = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_PREVIOUS_KEY)
        return Triple(notificationTitle, current, previous)
    }

    companion object {
        const val TEN_MINUTE: Long = 60000 * 10 // 10 min
        const val SCHEDULE_CURRENT_KEY = "schedule_current_key"
        const val SCHEDULE_PREVIOUS_KEY = "schedule_previous_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}