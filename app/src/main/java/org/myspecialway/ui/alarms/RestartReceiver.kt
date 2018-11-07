package org.myspecialway.ui.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class RestartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // make sure the daily alarms are triggered
        context?.scheduleAlarmOfAlarms()

        // if the device was booted get the jobs for today
        context?.scheduleDailyAlarms()

    }
}