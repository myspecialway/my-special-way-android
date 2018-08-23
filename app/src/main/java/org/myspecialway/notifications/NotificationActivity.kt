package org.myspecialway.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.ScheduleRenderModel


class NotificationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val (notificationTitle, destination) = getBundle(intent)
        notificationText.text = notificationTitle
        navigationButton.setOnClickListener { Navigation.toUnityNavigation(this, destination) }
    }

    private fun getBundle(intent: Intent): Pair<String, ScheduleRenderModel> {
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE)
        val destination = intent.getParcelableExtra<ScheduleRenderModel>(SCHEDULE_KEY)
        return Pair(notificationTitle, destination)
    }

    companion object {
        const val SCHEDULE_KEY = "schedule_key"
        const val NOTIFICATION_TITLE = "notification_title"
    }
}