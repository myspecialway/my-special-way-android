package org.myspecialway.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_notification.*

import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.ui.splash.SplashActivity


class NotificationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val intent = intent
        val destination = intent.getStringExtra(MESSAGE_TEXT)
        dialog_notification_text.text = destination
        dialog_notification_button
                .setOnClickListener { Navigation.toUnityNavigation(this, destination) }
    }

    companion object {
        const val MESSAGE_TEXT = "message_text"
    }
}