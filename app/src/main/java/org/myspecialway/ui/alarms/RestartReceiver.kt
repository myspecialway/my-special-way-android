package org.myspecialway.ui.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*


class RestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val now = Calendar.getInstance().time

        if (now.before(six())) {
            val service = Intent(context, AlarmService::class.java)
            context?.startService(service)
        }
    }

    private fun six(): Date? {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR_OF_DAY, 6)
        return cal.time
    }
}