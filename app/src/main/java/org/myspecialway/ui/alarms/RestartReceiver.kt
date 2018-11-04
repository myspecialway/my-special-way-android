package org.myspecialway.ui.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class RestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, AlarmService::class.java)
        context?.startService(intent)
    }
}